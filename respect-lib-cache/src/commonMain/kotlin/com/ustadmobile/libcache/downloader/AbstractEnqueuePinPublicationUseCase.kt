package com.ustadmobile.libcache.downloader

import com.ustadmobile.libcache.db.UstadCacheDb
import com.ustadmobile.libcache.db.entities.TransferJob
import com.ustadmobile.libcache.db.entities.TransferJobItem
import com.ustadmobile.libcache.db.entities.TransferJobItemStatus
import com.ustadmobile.libcache.util.withWriterTransaction
import io.ktor.http.Url
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 *
 */
@OptIn(ExperimentalTime::class)
abstract class AbstractEnqueuePinPublicationUseCase(
    private val db: UstadCacheDb
) {

    /**
     * @param manifestUrl the URL of the Readium Web Publication Manifest (that should
     */
    protected suspend fun createTransferJob(
        manifestUrl: Url,
    ): TransferJob {
        TODO()
//        db.withWriterTransaction {
//            val transferJob = TransferJob(
//                tjType = TransferJob.TYPE_DOWNLOAD,
//                tjStatus = TransferJobItemStatus.STATUS_QUEUED_INT,
//                tjTimeCreated = Clock.System.now().toEpochMilliseconds(),
//                tjManifestUrl = manifestUrl,
//                tjEntityUid = 0,
//                tjTableId = 0,
//                tjOiUid = 0,
//            )
//
//            val jobUid = db.transferJobDao.insert(transferJob).toInt()
//
//            val manifestTransferJobItem = TransferJobItem(
//                tjiTjUid = jobUid,
//                tjiSrc = manifestUrl.toString(),
//                tjiEntityUid = contentEntryVersion.cevUid,
//                tjiTableId = ContentEntryVersion.TABLE_ID,
//            )
//            db.transferJobItemDao().insert(manifestTransferJobItem)
//
//            transferJob.copy(
//                tjUid = jobUid
//            )
//        }
    }

}