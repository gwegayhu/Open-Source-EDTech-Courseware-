package world.respect.domain.opds.model

import com.eygraber.uri.Uri
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * Represents metadata for an OPDS catalog or collection.
 * This includes details like title, number of items, items per page, etc.
 *
 * For reference, see the schema: https://github.com/readium/webpub-manifest/blob/master/schema/metadata.schema.json
 */
@Serializable
data class OpdsFeedMetadata(
    val title: String,
    val identifier: Uri? = null,
    @SerialName("@type")
    val type: String? = null,
    val subtitle: String? = null,
    val numberOfItems: Int? = null,
    val itemsPerPage: Int? = null,
    val currentPage: Int? = null,

    val publisher: String? = null,

    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val author: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val translator: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val editor: List<OpdsContributor>? = null,

    val language: String? = null,
    val description: String? = null,
    @Contextual
    val modified: LocalDateTime? = null
)