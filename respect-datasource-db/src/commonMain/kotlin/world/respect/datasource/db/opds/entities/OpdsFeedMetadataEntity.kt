package world.respect.datasource.db.opds.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eygraber.uri.Uri
import kotlinx.datetime.LocalDateTime

/**
 * @property ofmeUid auto generated primary key
 * @property ofmeOfeUid the OpdsFeed uid (always set, even if this is for a facet or group)
 * @property ofmePropType the property type of this metadata
 * @property ofmeRelUid the uid for the facet or group (if applicable), otherwise 0
 */
@Entity
data class OpdsFeedMetadataEntity(
    @PrimaryKey(autoGenerate = true)
    val ofmeUid: Long = 0,
    val ofmeOfeUid: Long,
    val ofmePropType: PropType,
    val ofmeRelUid: Long,
    val ofmeIdentifier: Uri?,
    val ofmeType: String?,
    val ofmeTitle: String,
    val ofmeSubtitle: String?,
    val ofmeModified: LocalDateTime?,
    val ofmeDescription: String?,
    val ofmeItemsPerPage: Int?,
    val ofmeCurrentPage: Int?,
    val ofmeNumberOfItems: Int?
) {

    enum class PropType(val id: Int) {
        FEED_METADATA(1), FACET_METADATA(2), GROUP_METADATA(3)
    }

}