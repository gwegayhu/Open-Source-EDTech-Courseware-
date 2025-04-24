package world.respect.model

import kotlinx.serialization.Serializable

/**
 * Represents a Link Object in OPDS 2.0.
 * The href field is required, all other fields are optional.
 *
 * For reference, see the schema: https://readium.org/webpub-manifest/schema/link.schema.json
 */
@Serializable
data class OpdsLink(
    val href: String,                      // Required field (URL to the resource)
    @Serializable(with = RelTypeSerializer::class)
    val rel: RelType? = null,              // Optional field (defines the relationship)
    val type: String? = null,              // Optional field (type of the linked resource)
    val title: String? = null,             // Optional field (title of the linked resource)
    val templated: Boolean? = null,        // Optional field (indicates if the link is templated)
    val properties: OpdsLinkProperties? = null, // Optional additional properties for the link
    val height: Int? = null,               // Optional field (height for image links)
    val width: Int? = null                 // Optional field (width for image links)
)