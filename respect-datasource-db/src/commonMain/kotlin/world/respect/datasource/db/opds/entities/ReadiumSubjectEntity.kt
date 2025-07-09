package world.respect.datasource.db.opds.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eygraber.uri.Uri
import world.respect.datasource.db.opds.OpdsTopParentType

@Entity
data class ReadiumSubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val rseUid: Long,
    val rseTopParentType: OpdsTopParentType,
    val rseTopParentUid: Long,
    val rseSubjectSortAs: String?,
    val rseSubjectCode: String?,
    val rseSubjectScheme: Uri?,
) {

    companion object {

        const val TABLE_ID = 9

    }

}
