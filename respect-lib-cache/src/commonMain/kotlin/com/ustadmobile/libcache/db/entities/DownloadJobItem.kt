package com.ustadmobile.libcache.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import io.ktor.http.Url
import kotlinx.serialization.Serializable


/**
 * @param tjiUrl: the source of the TransferJobItem - the value of the string depends on the type
 * @param tjiDest: the destination of the TransferJobItem - the value of the string depends on the type
 * @param tjiTableId if not zero, BlobUploadClientUseCase will insert an OutgoingReplication when the
 *        TransferJobItem is complete. This can be useful when handling the upload of blobs that
 *        are associated with entities in the database such as PersonPicture. This will update the
 *        uri on the server after the blob itself is successfully uploaded.
 * @param tjiEntityUid used with tjiTableId
 * @param tjiEntityEtag the etag (e.g. field annotated @ReplicateEtag) of the entity for which this
 *        transfer is being performed. This ensures that when TransferJobItem is queried in order to
 *        display the status of an item for the user it only returns relevant status e.g.
 *        if a previous version failed/succeeded, but it was since replaced, the status of the
 *        transfer for the previous version is no longer relevant.
 * @param tjiLockIdToRelease when an upload is finished, then the retention lock that was created to
 *        prevent its eviction from the cache before upload is finished should be cleared.
 * @param tjiPartialTmpFile when a download is running, this is the file to use to store a partial
 *        response so it can be resumed if the download is interrupted.
 */
@Entity(
    indices = [
        Index("tjiTjUid", name="transferjob_tjuid"),
    ]
)
@Serializable
data class DownloadJobItem(
    @PrimaryKey(autoGenerate = true)
    val tjiUid: Int = 0,

    val tjiTjUid: Int = 0,

    val tjTotalSize: Long = 0,

    val tjTransferred: Long = 0,

    val tjAttemptCount: Int = 0,

    val tjiUrl: Url,

    val tjiDest: String? = null,

    val tjiType: Int = 0,

    val tjiStatus: Int = 0,

    //This should be set when the transferjobitem is created - by query.
    @ColumnInfo(defaultValue = "0")
    val tjiEntityEtag: Long = 0,

    @ColumnInfo(defaultValue = "0")
    val tjiLockIdToRelease: Long = 0,

    val tjiPartialTmpFile: String? = null,

    )
