package world.respect.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import world.respect.serialization.RelTypeSerializer

/**
 * Represents an OPDS 2.0 Catalog Feed.
 * According to the specification, an OPDS 2.0 Catalog Feed must:
 * - Contain at least one collection (navigation, publications or groups)
 * - Contain a title in its metadata
 * - Contain a reference to itself using a self link
 */
@Serializable
data class OpdsCatalog(
    val metadata: Metadata,
    val links: List<Link>,
    val navigation: List<Link>? = null,
    val publications: List<Publication>? = null,
    val facets: List<Facet>? = null,
    val groups: List<Group>? = null
) {
    companion object {
        const val MEDIA_TYPE = "application/opds+json"
    }
}

/**
 * Represents metadata for an OPDS catalog or collection.
 * The title is required, all other fields are optional.
 */
@Serializable
data class Metadata(
    val title: String,
    val numberOfItems: Int? = null,
    val itemsPerPage: Int? = null,
    val currentPage: Int? = null
)


/**
 * Represents a Link Object in OPDS 2.0.
 * The href field is required, all other fields are optional.
 */
@Serializable
data class Link(
    val href: String,
    @Serializable(with = RelTypeSerializer::class)
    val rel: RelType? = null,
    val type: String? = null,
    val title: String? = null,
    val templated: Boolean? = null,
    val properties: Properties? = null,
    val height: Int? = null,
    val width: Int? = null
)

/**
 * Represents the rel field which can be either a single string or an array of strings.
 */
sealed class RelType {

    /**
     * Checks if this relation contains the specified value.
     */
    abstract fun contains(value: String): Boolean

    /**
     * A single relation value.
     */
    data class Single(val value: String) : RelType() {
        override fun contains(value: String): Boolean = this.value == value
    }

    /**
     * Multiple relation values.
     */
    data class Multiple(val values: List<String>) : RelType() {
        override fun contains(value: String): Boolean = values.contains(value)
    }

    companion object {
        /**
         * Creates a RelType from a string or string array.
         */
        fun from(value: Any): RelType {
            return when (value) {
                is String -> Single(value)
                is List<*> -> Multiple(value.filterIsInstance<String>())
                else -> throw IllegalArgumentException("Unsupported type for rel: ${value::class}")
            }
        }
    }
}

/**
 * Represents a relation value, which can be either a single string or a list of strings.
 */
@Serializable
sealed class RelValue {
    @Serializable
    @SerialName("single")
    data class Single(val value: String) : RelValue()

    @Serializable
    @SerialName("multiple")
    data class Multiple(val values: List<String>) : RelValue()
}

/**
 * Represents properties that can be associated with a Link Object.
 * All fields are optional.
 */
@Serializable
data class Properties(
    val numberOfItems: Int? = null,
    val price: Price? = null,
    val indirectAcquisition: List<Acquisition>? = null
)

/**
 * Represents price information for acquisition links.
 */
@Serializable
data class Price(
    val currency: String,
    val value: Float
)

/**
 * Represents an Acquisition Object, indicating how a publication can be acquired.
 */
@Serializable
data class Acquisition(
    val type: String,
    val child: List<Acquisition>? = null
)

/**
 * Represents a Publication in an OPDS 2.0 catalog.
 */
@Serializable
data class Publication(
    val metadata: PublicationMetadata,
    val links: List<Link>,
    val images: List<Link>? = null
) {
    companion object {
        const val MEDIA_TYPE = "application/opds-publication+json"
    }
}

/**
 * Represents metadata for a Publication.
 */
@Serializable
data class PublicationMetadata(
    val title: String,
    @SerialName("@type")
    val type: String? = null,
    val identifier: String,
    val author: ContributorObject? = null,
    val translator: ContributorObject? = null,
    val editor: ContributorObject? = null,
    val artist: ContributorObject? = null,
    val illustrator: ContributorObject? = null,
    val letterer: ContributorObject? = null,
    val penciler: ContributorObject? = null,
    val colorist: ContributorObject? = null,
    val inker: ContributorObject? = null,
    val narrator: ContributorObject? = null,
    val publisher: PublisherObject? = null,
    val language: String? = null,
    val modified: String? = null,
    val description: String? = null,
    val belongsTo: BelongsTo? = null
)

/**
 * Represents a contributor (author, translator, etc.) with optional links.
 */
@Serializable
data class ContributorObject(
    val name: String,
    val identifier: String? = null,
    val sortAs: String? = null,
    val links: List<Link>? = null
)

/**
 * Represents a publisher with optional links.
 */
@Serializable
data class PublisherObject(
    val name: String,
    val links: List<Link>? = null
)

/**
 * Represents a "belongsTo" relationship, indicating series or collection membership.
 */
@Serializable
data class BelongsTo(
    val series: Series? = null,
    val collection: String? = null
)

/**
 * Represents a series that a publication belongs to.
 */
@Serializable
data class Series(
    val name: String,
    val position: Int? = null,
    val links: List<Link>? = null
)

/**
 * Represents a Facet group in an OPDS 2.0 catalog.
 */
@Serializable
data class Facet(
    val metadata: Metadata,
    val links: List<Link>
)

/**
 * Represents a Group in an OPDS 2.0 catalog.
 * A group must contain either a navigation collection or a publications collection.
 */
@Serializable
data class Group(
    val metadata: Metadata,
    val links: List<Link>? = null,
    val navigation: List<Link>? = null,
    val publications: List<Publication>? = null
)