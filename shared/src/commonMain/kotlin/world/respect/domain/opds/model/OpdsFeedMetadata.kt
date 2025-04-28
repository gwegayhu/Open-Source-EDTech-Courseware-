package world.respect.domain.opds.model

import kotlinx.serialization.Serializable

/**
 * Represents metadata for an OPDS catalog or collection.
 * This includes details like title, number of items, items per page, etc.
 *
 * For reference, see the schema: https://drafts.opds.io/schema/feed-metadata.schema.json
 */
@Serializable
data class OpdsFeedMetadata(
    val title: String,                     // Required field (title of the catalog or collection)
    val numberOfItems: Int? = null,        // Optional (total number of items in the collection)
    val itemsPerPage: Int? = null,         // Optional (items per page in the collection)
    val currentPage: Int? = null           // Optional (current page number)
)