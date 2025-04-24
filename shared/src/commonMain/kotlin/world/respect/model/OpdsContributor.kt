package world.respect.model

import kotlinx.serialization.Serializable

/**
 * Represents a contributor (author, translator, etc.) with optional links.
 *
 * For reference, see the schema: https://drafts.opds.io/schema/publication.schema.json
 */
@Serializable
data class OpdsContributor(
    val name: String,                      // Required field (name of the contributor)
    val identifier: String? = null,        // Optional field (identifier for the contributor)
    val sortAs: String? = null,            // Optional field (sorting name for the contributor)
    val links: List<OpdsLink>? = null      // Optional field (links to further information)
)