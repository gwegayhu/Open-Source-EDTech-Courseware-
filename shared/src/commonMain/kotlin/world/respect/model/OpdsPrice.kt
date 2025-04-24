package world.respect.model

import kotlinx.serialization.Serializable

/**
 * Represents price information for acquisition links.
 *
 * For reference, see the schema: https://drafts.opds.io/schema/feed.schema.json
 */
@Serializable
data class OpdsPrice(
    val currency: String,  // Required field (currency code, e.g., USD, EUR)
    val value: Float       // Required field (price value)
)