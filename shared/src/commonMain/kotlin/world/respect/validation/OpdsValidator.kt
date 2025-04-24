package world.respect.validation

import world.respect.model.OpdsCatalog
import world.respect.model.Publication


/**
 * Validates OPDS catalogs and publications according to the OPDS 2.0 specification.
 */
class OpdsValidator {

    /**
     * Validates an OPDS catalog against the specification requirements.
     *
     * According to OPDS 2.0, a valid catalog must:
     * - Contain at least one collection identified by navigation, publications, or groups
     * - Contain a title in its metadata
     * - Contain a reference to itself using a self link
     *
     * @param catalog The catalog to validate
     * @return A Result object containing either Unit (success) or a ValidationException (failure)
     */
    fun validateCatalog(catalog: OpdsCatalog): Result<Unit> {
        val errors = mutableListOf<String>()

        // Check for title in metadata
        if (catalog.metadata.title.isBlank()) {
            errors.add("Catalog metadata must contain a non-blank title")
        }

        // Check for self link using pattern matching with safe access
        val hasSelfLink = catalog.links.any { link ->
            link.rel?.contains("self") == true
        }

        if (!hasSelfLink) {
            errors.add("Catalog must contain a self link")
        }

        // Check for at least one required collection
        val hasRequiredCollection = catalog.navigation != null ||
                catalog.publications != null ||
                (catalog.groups != null && catalog.groups.isNotEmpty())

        if (!hasRequiredCollection) {
            errors.add("Catalog must contain at least one collection with role: navigation, publications, or groups")
        }

        return if (errors.isEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(ValidationException(errors))
        }
    }

    /**
     * Validates an OPDS publication against the specification requirements.
     *
     * According to OPDS 2.0, a valid publication must:
     * - Contain at least one acquisition link
     * - Should contain a self link
     *
     * @param publication The publication to validate
     * @return A Result object containing either Unit (success) or a ValidationException (failure)
     */
    fun validatePublication(publication: Publication): Result<Unit> {
        val errors = mutableListOf<String>()

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

        // Check for self link (warning only, not required)
        val hasSelfLink = publication.links.any { link ->
            link.rel?.contains("self") == true
        }

        if (!hasSelfLink) {
            // This is a warning, not an error
            println("Warning: Publication should contain a self link")
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