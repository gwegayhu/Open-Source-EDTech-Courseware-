package world.respect.datalayer.db.compatibleapps.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eygraber.uri.Uri
import io.ktor.http.Url

/**
 * @property caeUid the xxhash of the caeUrl
 * @property caeUrl the URL of the manifest
 */
@Entity
data class CompatibleAppEntity(
    @PrimaryKey
    val caeUid: Long,
    val caeUrl: Url,
    val caeIcon: Uri?,
    val caeLastModified: Long,
    val caeEtag: String?,
    val caeLicense: String,
    val caeWebsite: String,
    val caeLearningUnits: String,
    val caeDefaultLaunchUri: String,
    val caeAndroidPackageId: String?,
    val caeAndroidStoreList: String?,
    val caeAndroidSourceCode: String?,
) {

    companion object {

        const val TABLE_ID = 42

    }

}