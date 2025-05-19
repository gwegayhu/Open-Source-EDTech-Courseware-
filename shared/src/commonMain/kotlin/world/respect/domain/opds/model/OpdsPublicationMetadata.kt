package world.respect.domain.opds.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Represents metadata for a Publication.
 * For reference, see the schema: https://drafts.opds.io/opds-2.0#52-metadata
 * Schema: https://readium.org/webpub-manifest/schema/metadata.schema.json
 */
@Serializable
data class OpdsPublicationMetadata(
    val title: String,                      // Required field (title of the publication)
    @SerialName("@type")
    val type: String? = null,
    val identifier: String,                 // Required field (unique identifier for the publication)
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val author: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val translator: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val editor: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val artist: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val illustrator: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val letterer: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val penciler: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val colorist: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val inker: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val narrator: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val contributor: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val publisher: List<OpdsContributor>? = null,
    @Serializable(with = OpdsSingleItemToListTransformer::class)
    val imprint: List<OpdsContributor>? = null,
    @Serializable(with = ReadiumSubjectToListTransformer::class)
    val subject: List<ReadiumSubject>? = null,
    val language: String? = null,
    val modified: String? = null,
    val published: String? = null,
    val description: String? = null,
    val belongsTo: OpdsBelongsTo? = null,
    val numberOfPages: Int? = null,
    val duration: Double? = null,
)