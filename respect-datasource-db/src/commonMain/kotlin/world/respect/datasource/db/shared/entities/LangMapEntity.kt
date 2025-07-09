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

    enum class TopParentType(val id: Int) {
        RESPECT_MANIFEST(1), OPDS_FEED(2), OPDS_PUBLICATION(3)
    }

    enum class PropType(val id: Int) {
        RESPECT_MANIFEST_NAME(1), RESPECT_MANIFEST_DESCRIPTION(2),

        READIUM_SUBJECT_NAME(3),

        OPDS_PUB_TITLE(4), OPDS_PUB_SORT_AS(5), OPDS_PUB_SUBTITLE(6),
    }


    companion object {

        const val LANG_NONE = ""

    }

}