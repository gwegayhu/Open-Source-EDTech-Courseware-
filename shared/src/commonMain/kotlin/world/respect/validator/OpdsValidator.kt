package world.respect.validator

import world.respect.model.OpdsCatalog
import world.respect.model.OpdsPublication

/**
 * Validates OPDS catalogs and publications according to the OPDS 2.0 specification.
 * In RESPECT context, OPDS catalogs are used to list learning units, where each
 * publication represents a distinct learning unit.
 * For reference, see the schemas:
 * - https://drafts.opds.io/schema/feed.schema.json
 * - https://drafts.opds.io/schema/publication.schema.json
 */
class OpdsValidator {

    /**
     * Validates an OPDS catalog against the specification requirements.
     * According to OPDS 2.0, a valid catalog must:
     * - Contain at least one collection identified by navigation, publications, or groups
     * - Contain a title in its metadata
     * - Contain a reference to itself using a self link
     * @param catalog The catalog to validate
     * @return A Result object containing either Unit (success) or a ValidationException (failure)
     */
    fun validateCatalog(catalog: OpdsCatalog): Result<Unit> {
        val errors = mutableListOf<String>()

        // Ensure metadata and links are not null and valid
        if (catalog.metadata.title.isBlank()) {
            errors.add("Catalog metadata must contain a non-blank title")
        }

        if (catalog.links.isEmpty()) {
            errors.add("Catalog must contain at least one link")
        }

        // Check for self link
        val hasSelfLink = catalog.links.any { link ->
            link.rel?.contains("self") == true
        }

        if (!hasSelfLink) {
            errors.add("Catalog must contain a self link")
        }

        // Ensure that at least one collection is present (publications, navigation, or groups)
        val hasRequiredCollection = catalog.publications != null || catalog.navigation != null || catalog.groups != null
        if (!hasRequiredCollection) {
            errors.add("Catalog must contain at least one collection (publications, navigation, or groups)")
        }

        // Validate collections (facets, groups, publications, navigation)
        catalog.facets?.forEach { facet ->
            if (facet.metadata.title.isBlank()) {
                errors.add("Each facet must contain a title in its metadata")
            }
            if (facet.links.size < 2) {
                errors.add("Each facet group should contain at least two or more Link Objects in links")
            }
        }

        catalog.groups?.forEach { group ->
            if (group.metadata.title.isBlank()) {
                errors.add("Each group must contain a title in its metadata")
            }
        }

        // Validate publications if present
        catalog.publications?.forEach { publication ->
            val publicationResult = validatePublication(publication)
            if (publicationResult.isFailure) {
                val exception = publicationResult.exceptionOrNull() as? ValidationException
                exception?.errors?.forEach { error ->
                    errors.add("Publication error: $error")
                }
            }
        }

        return if (errors.isEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(ValidationException(errors))
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