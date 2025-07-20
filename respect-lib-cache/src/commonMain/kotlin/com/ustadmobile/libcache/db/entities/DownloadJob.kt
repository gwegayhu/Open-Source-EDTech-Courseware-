package com.ustadmobile.libcache.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ktor.http.Url
import kotlinx.serialization.Serializable

/**
 * To be used by BlobUploadClient and content downloader. Provides database storage so that:
 *  - The job can be enqueued and run at an appropriate time (e.g. when connectivity is available)
 *  - The job can be interrupted and resumed
 *  - The progress of the job can be displayed to the user.
 *
 * A TransferJob has one or more TransferJobItem(s).
 *
 * @param tjStatus Status int as per TransferJobItemStatus
 * @param tjTableId Where this transfer job is associated with one specific entity, the tableId (optional)
 * @param tjEntityUid Where this transfer job is associated with one specific entity, the entity uid field (optional)
 *
 * @param tjPubManifestUrl Where this TransferJob is being used to pin a publication, the manifest url,
 *        otherwise, null.
 */
@Entity
/**
 * @param tjOiUid Id of the related OfflineItem (if any)
 */
@Serializable
data class DownloadJob(
    @PrimaryKey(autoGenerate = true)
    val tjUid: Int = 0,
    val tjType: Int = 0,
    val tjStatus: Int = 0,
    val tjName: String? = null,
    val tjPubManifestUrl: Url? = null,
    @ColumnInfo(defaultValue = "0")
    val tjTimeCreated: Long = 0,
    @ColumnInfo(defaultValue = "0")
    val tjCreationType: Int = 0,
) {
    companion object {
        //This entity is not replicated, however, this can be used as part of the key in lists
        const val TABLE_ID = 1081

        const val TYPE_BLOB_UPLOAD = 1

        const val TYPE_DOWNLOAD = 2


        const val CREATION_TYPE_USER = 1

        const val CREATION_TYPE_UPDATE = 2
    }

}