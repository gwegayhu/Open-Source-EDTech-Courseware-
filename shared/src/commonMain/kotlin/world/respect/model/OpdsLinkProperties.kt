package world.respect.model

import kotlinx.serialization.Serializable

/**
 * Represents properties that can be associated with a Link Object.
 * Includes details like number of items, price, and indirect acquisition information.
 *
 * For reference, see the schema: https://drafts.opds.io/schema/feed.schema.json
 */
@Serializable
data class OpdsLinkProperties(
    val numberOfItems: Int? = null,
    val price: OpdsPrice? = null,
    val indirectAcquisition: List<OpdsAcquisition>? = null
)