package world.respect.datasource.db.compatibleapps.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eygraber.uri.Uri
import io.ktor.http.Url

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