package world.respect.datasource.db.opds.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database Entity that represents a ReadiumLink. ReadiumLinks are always associated with an
 * OpdsFeed or OpdsPublication (handled by the rleTableId and rleEntityId fields).
 *
 * A ReadiumLink can join to other ReadiumLinks e.g. via children, alternate, or subcollection (
 * which is a recursive structure as per the ReadliumLink JSON spec). This is handled by the
 * rleJoinToLinkId and rleJoinToLinkType fields.
 *
 * @property rleTableId the table id for the entity this is part of OpdsPublication or OpdsFeed
 * @property rleEntityId entityId for the entity this is part of OpdsPublication or OpdsFeed
 * @property rleJoinToLinkId where this link is a child of another link e.g. part of its
 *           alternate, children, or subcollection links, then this provides the id of the other
 *           link
 * @property rleJoinToLinkType
 *
 */
@Entity
class ReadiumLinkEntity(
    @PrimaryKey(autoGenerate = true)
    val rleId: Long,

    val rleTableId: Int,

    val rleEntityId: Long,

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

    enum class LinkEntityJoinType {
        ALTERNATE_OF,
        CHILDREN_OF,
        SUB_COLLECTION_OF
    }


    companion object {

        const val TABLE_ID = 2

    }

}