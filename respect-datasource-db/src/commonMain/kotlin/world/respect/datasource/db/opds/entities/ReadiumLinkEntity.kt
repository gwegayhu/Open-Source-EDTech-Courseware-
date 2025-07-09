package world.respect.datasource.db.opds.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import world.respect.datasource.db.opds.OpdsTopParentType

/**
 * Database Entity that represents a ReadiumLink. ReadiumLinks are always associated with an
 * OpdsFeed or OpdsPublication (handled by the rleTableId and rleEntityId fields).
 *
 * A ReadiumLink can join to other ReadiumLinks e.g. via children, alternate, or subcollection (
 * which is a recursive structure as per the ReadliumLink JSON spec). This is handled by the
 * rleJoinToLinkId and rleJoinToLinkType fields.
 *
 * @property rleTopParentType the top parent type (opds feed or publication)
 * @property rleTopParentUid entityId for the entity this is part of OpdsPublication or OpdsFeed
 * @property rleJoinToLinkId where this link is a child of another link e.g. part of its
 *           alternate, children, or subcollection links, then this provides the id of the other
 *           link
 * @property rleJoinToLinkType
 * @property rlePropType The property type where this link has been used
 *
 */
@Entity
class ReadiumLinkEntity(
    @PrimaryKey(autoGenerate = true)
    val rleId: Long,

    val rleTopParentType: OpdsTopParentType,

    val rleTopParentUid: Long,

    val rlePropType: PropertyType,

    val rleJoinToLinkId: Long = 0,

    val rleJoinToLinkType: LinkEntityJoinType? = null,

    val rleIndex: Int,

    val rleHref: String,

    val rleRel: List<String>?,

    val rleType: String? = null,

    val rleTitle: String? = null,

    val rleTemplated: Boolean? = null,

    val rleProperties: String? = null,

    val rleHeight: Int? = null,

    val rleWidth: Int? = null,

    val rleSize: Int? = null,

    val rleBitrate: Double? = null,

    val rleDuration: Double? = null,

    val rleLanguage: List<String>? = null,
) {

    /**
     * Property types used when a ReadiumLinkEntity is joined to another ReadiumLinkEntity (to
     * represent the alternate, children, and subcollection properties of ReadiumLink),
     */
    enum class LinkEntityJoinType {
        ALTERNATE_OF,
        CHILDREN_OF,
        SUB_COLLECTION_OF
    }

    /**
     * ReadiumLink can be contained directly by an OpdsFeed or OpdsPublication, or by subelements
     * thereof. The ReadiumLinkEntity.rleTableId and ReadiumLinkEntity.rleEntityUid fields will
     * ALWAYS connect to the OpdsFeed or OpdsPublication (such that they can be queried) accordingly.
     */
    @Suppress("unused")
    enum class PropertyType {
        OPDS_PUB_LINKS, OPDS_PUB_IMAGES, OPDS_PUB_READING_ORDER, OPDS_PUB_RESOURCES, OPDS_PUB_TOC,
        OPDS_PUB_SUBJECT_LINKS,

        OPDS_PUBLICATION, OPDS_FACET, OPDS_GROUP, OPDS_SERIES, READIUM_CONTRIBUTOR,
        READIUM_SUBJECT_LINKS
    }

    companion object {

        const val TABLE_ID = 2

    }

}