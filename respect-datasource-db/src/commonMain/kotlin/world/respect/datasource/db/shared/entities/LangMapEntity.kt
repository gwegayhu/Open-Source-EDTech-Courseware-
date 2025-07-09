package world.respect.datasource.db.shared.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents entries in a LangMap structure as per
 * https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#42-language-maps
 *
 * LangMaps are used on the RespectAppManifest, OPDS, and various Xapi entities.
 *
 * @property lmeTopParentType a unique table id (e.g. using TABLE_ID constants)
 * @property lmeTopParentUid1 the uid of the related entity (e.g. RespectAppManifest, OpdsPublication, etc)
 * @property lmeTopParentUid2 second uid of the related entity, where applicable (e.g. Xapi Statements UUID)
 * @property lmePropType Entities have multiple properties that use a LangMap, this field can be used to differentiate
 * @property lmeLang Language code (e.g. en)
 * @property lmeRegion Region code (e.g. US)
 * @property lmeValue the actual string value
 */
@Entity(
    indices = [Index(value = ["lmeTopParentType", "lmeTopParentUid1", "lmeTopParentUid2", "lmePropType"])]
)
data class LangMapEntity(
    @PrimaryKey(autoGenerate = true)
    val lmeId: Long = 0,
    val lmeTopParentType: TopParentType,
    val lmeTopParentUid1: Long,
    val lmeTopParentUid2: Long = 0,
    val lmePropType: PropType,
    val lmeLang: String,
    val lmeRegion: String?,
    val lmeValue: String,
) {

    enum class TopParentType {
        RESPECT_MANIFEST, OPDS_FEED, OPDS_PUBLICATION
    }

    enum class PropType {
        RESPECT_MANIFEST_NAME, RESPECT_MANIFEST_DESCRIPTION,
    }


    companion object {

        const val LANG_NONE = ""

    }

}