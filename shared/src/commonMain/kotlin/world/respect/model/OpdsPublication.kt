package world.respect.model

import kotlinx.serialization.Serializable

/**
 * Represents a Publication in an OPDS 2.0 catalog.
 * In RESPECT context, each publication represents a Learning Unit.
 *
 * For reference, see the schema: https://drafts.opds.io/schema/publication.schema.json
 */
@Serializable
data class OpdsPublication(
    val metadata: OpdsPublicationMetadata,  // Required field
    val links: List<OpdsLink>,              // Required field
    val images: List<OpdsLink>? = null      // Optional field
) {
    init {
        // Ensure metadata and links are not null and have content
        require(metadata.title.isNotBlank()) { "Publication metadata must contain a non-blank title" }
        require(links.isNotEmpty()) { "Publication must contain at least one link" }
    }
}
