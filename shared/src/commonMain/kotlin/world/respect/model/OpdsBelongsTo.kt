package world.respect.model

import kotlinx.serialization.Serializable

/**
 * Represents a "belongsTo" relationship, indicating series or collection membership.
 *
 * For reference, see the schema: https://drafts.opds.io/schema/publication.schema.json
 */
@Serializable
data class OpdsBelongsTo(
    val series: OpdsSeries? = null,         // Optional field (indicates the series)
    val collection: String? = null          // Optional field (indicates the collection)
)