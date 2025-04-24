package world.respect.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Represents metadata for a Publication.
 *
 * For reference, see the schema: https://drafts.opds.io/schema/publication.schema.json
 */
@Serializable
data class OpdsPublicationMetadata(
    val title: String,                    // Required field (title of the publication)
    @SerialName("@type")
    val type: String? = null,             // Optional field (type of publication)
    val identifier: String,               // Required field (unique identifier for the publication)
    val author: OpdsContributor? = null,    // Optional field (author of the publication)
    val language: String? = null,           // Optional field (language of the publication)
    val description: String? = null,        // Optional field (description of the publication)
    val belongsTo: OpdsBelongsTo? = null    // Optional field (indicates series or collection)
)