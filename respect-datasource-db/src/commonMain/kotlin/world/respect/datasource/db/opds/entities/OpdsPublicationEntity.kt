package world.respect.datasource.db.opds.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eygraber.uri.Uri
import io.ktor.http.Url

/**
 * @property opeUid auto generated primary key
 * @property opeOfeUid where this OpdsPublicationEntity is part of a Feed, the UID of the feed,
 *           otherwise 0
 * @property opeOfeIndex where this OpdsPublicationEntity is part of a Feed, the index of this
 *           publication within the feed.
 * @property
 */
@Entity
class OpdsPublicationEntity(
    @PrimaryKey(autoGenerate = true)
    val opeUid: Long = 0,
    val opeOfeUid: Long,
    val opeOfeIndex: Int,
    val opeUrl: Url?,
    val opeUrlHash: Long,
    val opeIdentifier: Uri?,
    val opeLanguage: List<String>?,
    val opeSubjectSortAs: String?,
    val opeSubjectCode: String?,
    val opeSubjectScheme: Uri?,
    val opeMdType: Uri? = null,
) {


    companion object {

        const val TABLE_ID = 12

        /**
         * Langmap constants are used for LangMapEntity.lmePropId when joining to
         * LangMapEntity
         */
        const val LANGMAP_PROP_TITLE = 1

        const val LANGMAP_PROP_SORTAS = 2

        const val LANGMAP_PROP_SUBJECT_NAME = 3


    }
}