package world.respect.domain.opds.validator

import world.respect.domain.opds.model.OpdsFeed
import world.respect.domain.opds.model.OpdsFeedMetadata
import world.respect.domain.opds.model.OpdsPublication

/**
 * Validates OPDS feeds and publications according to the OPDS 2.0 specification.
 * In RESPECT context, OPDS feeds are used to list learning units, where each
 * publication represents a distinct learning unit.
 * For reference, see the schemas:
 * - https://drafts.opds.io/schema/feed.schema.json
 * - https://drafts.opds.io/schema/publication.schema.json
 */
class OpdsValidator {

    // List to store warnings that don't cause validation failure
    private val warnings = mutableListOf<String>()

    /**
     * Validates an OPDS feed against the specification requirements.
     * According to OPDS 2.0, a valid feed must:
     * - Contain at least one collection identified by navigation, publications, or groups
     * - Contain a title in its metadata
     * - Contain a reference to itself using a self link
     * @param feed The feed to validate
     * @return A Result object containing either Unit (success) or a ValidationException (failure)
     */
    fun validateFeed(feed: OpdsFeed): Result<Unit> {
        warnings.clear()
        val errors = mutableListOf<String>()

        // Ensure metadata and links are not null and valid
        if (feed.metadata.title.isBlank()) {
            errors.add("Feed metadata must contain a non-blank title")
        }

        // Links contains rel = self
        val hasSelfLink = feed.links.any { it.hasRel("self") }
        if (!hasSelfLink) {
            errors.add("Feed must contain at least one 'self' link")
        }

        // At least one required collection
        if (feed.navigation == null && feed.publications == null && feed.groups == null) {
            errors.add("Feed must contain at least one of: navigation, publications, or groups")
        }

        // Validate contributors (author, translator, editor, etc.)
        validateContributors(feed.metadata, errors)

        return if (errors.isEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(ValidationException(errors))
        }
    }

    /**
     * Validates contributors (author, translator, publisher) in the metadata.
     * @param metadata The metadata to validate
     * @param errors The list of errors to collect validation issues
     */
    private fun validateContributors(metadata: OpdsFeedMetadata, errors: MutableList<String>) {
        // Publisher should be a string (non-blank)
        metadata.publisher?.let {
            if (it.isBlank()) {
                errors.add("Publisher cannot be blank")
            }
        }

        // Check for valid contributors (author, translator, etc.)
        metadata.author?.let {
            if (it.name.isBlank()) {
                errors.add("Author name cannot be blank")
            }
        }

        metadata.translator?.let {
            if (it.name.isBlank()) {
                errors.add("Translator name cannot be blank")
            }
        }

        metadata.editor?.let {
            if (it.name.isBlank()) {
                errors.add("Editor name cannot be blank")
            }
        }
    }

    /**
     * Validates an OPDS publication against the specification requirements.
     * In RESPECT context, each publication represents a Learning Unit.
     * According to OPDS 2.0, a valid publication must:
     * - Contain at least one acquisition link
     * - Should contain a self link
     * - When images are present, at least one must be in a required format
     * @param publication The publication to validate
     * @return A Result object containing either Unit (success) or a ValidationException (failure)
     */
    fun validatePublication(publication: OpdsPublication): Result<Unit> {
        warnings.clear()
        val errors = mutableListOf<String>()

        // Check that metadata and links are not null or empty
        if (publication.metadata.title.isBlank()) {
            errors.add("Publication metadata must contain a non-blank title")
        }

        if (publication.metadata.identifier.isBlank()) {
            errors.add("Publication metadata must contain a non-blank identifier")
        }

        if (publication.links.isEmpty()) {
            errors.add("Publication must contain at least one link")
        }

        // Check for acquisition links
        val acquisitionRelations = listOf(
            "http://opds-spec.org/acquisition",
            "http://opds-spec.org/acquisition/open-access",
            "http://opds-spec.org/acquisition/borrow",
            "http://opds-spec.org/acquisition/buy",
            "http://opds-spec.org/acquisition/sample",
            "preview",
            "http://opds-spec.org/acquisition/subscribe"
        )

        val hasAcquisitionLink = publication.links.any { link ->
            acquisitionRelations.any { rel -> link.rel?.contains(rel) == true }
        }

        if (!hasAcquisitionLink) {
            errors.add("Publication must contain at least one acquisition link")
        }

        // Check for self link (warning only, doesn't add to errors)
        val hasSelfLink = publication.links.any { link -> link.rel?.contains("self") == true }
        if (!hasSelfLink) {
            warnings.add("Publication should contain a self link")
        }

        // Check image formats when images are present
        if (publication.images != null && publication.images.isNotEmpty()) {
            val hasRequiredImageFormat = publication.images.any { link ->
                val type = link.type
                type == "image/jpeg" || type == "image/avif" ||
                        type == "image/png" || type == "image/gif"
            }

            if (!hasRequiredImageFormat) {
                errors.add("Publications with images must include at least one image in JPEG, AVIF, PNG, or GIF format")
            }
        }

        return if (errors.isEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(ValidationException(errors))
        }
    }

    /**
     * Exception thrown when validation fails.
     */
    class ValidationException(val errors: List<String>) : Exception(errors.joinToString("\n"))
}