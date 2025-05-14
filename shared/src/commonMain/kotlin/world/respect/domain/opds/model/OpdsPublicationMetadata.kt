package world.respect.domain.opds.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Represents metadata for a Publication.
 * For reference, see the schema: https://drafts.opds.io/opds-2.0#52-metadata
 */
@Serializable
data class OpdsPublicationMetadata(
    val title: String,                      // Required field (title of the publication)
    @SerialName("@type")
    val type: String? = null,               // Optional field (type of publication)
    val identifier: String,                 // Required field (unique identifier for the publication)
    //TODO: OpdsContributor is polymorphic ; can be just a string
//    val author: OpdsContributor? = null,    // Optional field (author of the publication)
//    val translator: OpdsContributor? = null, // Optional field
//    val editor: OpdsContributor? = null,     // Optional field
//    val artist: OpdsContributor? = null,     // Optional field
//    val illustrator: OpdsContributor? = null, // Optional field
//    val letterer: OpdsContributor? = null,   // Optional field
//    val penciler: OpdsContributor? = null,   // Optional field
//    val colorist: OpdsContributor? = null,   // Optional field
//    val inker: OpdsContributor? = null,      // Optional field
//    val narrator: OpdsContributor? = null,   // Optional field
    val publisher: String? = null,          // Optional field (publisher of the publication)
    val language: String? = null,           // Optional field (language of the publication)
    val modified: String? = null,           // Optional field (last modification date)
    val description: String? = null,        // Optional field (description of the publication)
    val belongsTo: OpdsBelongsTo? = null    // Optional field (indicates series or collection)
)