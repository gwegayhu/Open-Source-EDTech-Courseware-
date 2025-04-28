package world.respect.domain.opds.model

import kotlinx.serialization.Serializable

/**
 * Represents metadata for an OPDS catalog or collection.
 * This includes details like title, number of items, items per page, etc.
 *
 * For reference, see the schema: https://github.com/readium/webpub-manifest/blob/master/schema/metadata.schema.json
 */
@Serializable
data class OpdsFeedMetadata(
    val title: String,                     // Required field (title of the catalog or collection)
    val numberOfItems: Int? = null,        // Optional (total number of items in the collection)
    val itemsPerPage: Int? = null,         // Optional (items per page in the collection)
    val currentPage: Int? = null,          // Optional (current page number)

    val publisher: String? = null,         // Publisher is a simple String (e.g., "SciFi Publishing Inc.")

    val author: OpdsContributor? = null,   // Optional contributor field (can be multiple, but for now it's just one)
    val translator: OpdsContributor? = null,  // Optional contributor field
    val editor: OpdsContributor? = null,   // Optional contributor field

    val language: String? = null,          // Optional language
    val description: String? = null,       // Optional description
    val modified: String? = null           // Optional modified date
)