package com.ustadmobile.libcache.downloader

import com.ustadmobile.libcache.EntryLockRequest
import com.ustadmobile.libcache.UstadCache
import com.ustadmobile.libcache.db.UstadCacheDb
import com.ustadmobile.libcache.db.entities.DownloadJobItem
import com.ustadmobile.libcache.db.entities.PinnedPublication
import com.ustadmobile.libcache.util.withWriterTransaction
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import world.respect.lib.opds.model.OpdsPublication
import world.respect.libutil.ext.resolve

/**
 * Fetches the publication manifest (over http) and goes through all of the resource links.
 * For each resource:
 *
 *  a) Create a DownloadJobItem
 *  b) Add a RetentionLock
 *  c) If the resource does not have a size provided in the ReadiumLink, then make an HTTP HEAD request
 *     to get the size and update the TransferJobItem accordingly.
 *  d) Enqueue RunDownloadJob to run the actual download.
 */
class PinPublicationPrepareUseCase(
    private val httpClient: HttpClient,
    private val db: UstadCacheDb,
    private val cache: UstadCache,
    private val enqueueRunDownloadJobUseCase: EnqueueRunDownloadJobUseCase,
) {

    /**
     *
     */
    suspend operator fun invoke(
        downloadJobUid: Int
    ) {
        val downloadJob = db.downloadJobDao.findByUid(downloadJobUid)
            ?: throw IllegalArgumentException("No transfer job with uid $downloadJobUid")
        val manifestUrl = downloadJob.djPubManifestUrl
            ?: throw IllegalArgumentException("no manifest url")

        val publication: OpdsPublication = httpClient.get(manifestUrl).body()

        //Pending: use http HEAD request to get
        //Could use https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-dispatcher/limited-parallelism.html#

        val downloadJobItems = publication.resources?.map { resource ->
            DownloadJobItem(
                djiDjUid = downloadJobUid,
                djiUrl = manifestUrl.resolve(resource.href),
                djiTotalSize = (resource.size ?: 0).toLong()
            )
        } ?: emptyList()

        cache.addRetentionLocks(
            downloadJobItems.map {
                EntryLockRequest(
                    url = it.djiUrl.toString(),
                    publicationUid = downloadJob.djPubManifestHash,
                )
            }
        )

        db.withWriterTransaction {
            db.downloadJobItemDao.insertList(downloadJobItems)
            db.pinnedPublicationDao.insert(
                PinnedPublication(
                    ppUrlHash = downloadJob.djPubManifestHash,
                    title = downloadJob.djName ?: ""
                )
            )
        }

        enqueueRunDownloadJobUseCase(downloadJobUid)
    }

}