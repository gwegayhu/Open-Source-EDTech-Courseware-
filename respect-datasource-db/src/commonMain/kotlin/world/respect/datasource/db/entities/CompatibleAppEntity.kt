package world.respect.datasource.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompatibleAppEntity(
    @PrimaryKey
    val caeUid: Long,
    val caeUrl: String,
    val caeLastModified: Long,
    val caeEtag: String?,
    val caeLicense: String,
    val caeWebsite: String,
    val caeLearningUnits: String,
    val caeDefaultLaunchUri: String,
    val caeAndroidPackageId: String?,
    val caeAndroidStoreList: String?,
    val caeAndroidSourceCode: String?,
)

