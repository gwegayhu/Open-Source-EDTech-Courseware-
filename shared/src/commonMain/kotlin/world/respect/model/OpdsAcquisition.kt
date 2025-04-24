package world.respect.model

import kotlinx.serialization.Serializable

/**
 * Represents an Acquisition Object, indicating how a publication can be acquired.
 *
 * For reference, see the schema: https://drafts.opds.io/schema/feed.schema.json
 */
@Serializable
data class OpdsAcquisition(
    val type: String,                      // Required field (type of acquisition)
    val child: List<OpdsAcquisition>? = null // Optional (additional acquisition links if needed)
)