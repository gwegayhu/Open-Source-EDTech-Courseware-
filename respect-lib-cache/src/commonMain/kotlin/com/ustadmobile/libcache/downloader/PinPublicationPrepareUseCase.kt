package com.ustadmobile.libcache.downloader

import com.ustadmobile.libcache.db.UstadCacheDb
import com.ustadmobile.libcache.db.entities.DownloadJobItem
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
 */
class PinPublicationPrepareUseCase(
    private val httpClient: HttpClient,
    private val db: UstadCacheDb,
) {

    /**
     *
     */
    suspend operator fun invoke(
        downloadJobUid: Int
    ) {
        val transferJob = db.downloadJobDao.findByUid(downloadJobUid)
            ?: throw IllegalArgumentException("No transfer job with uid $downloadJobUid")
        val manifestUrl = transferJob.djPubManifestUrl
            ?: throw IllegalArgumentException("no manifest url")

        val publication: OpdsPublication = httpClient.get(manifestUrl).body()
        val downloadJobItems = publication.resources?.map { resource ->
            DownloadJobItem(
                djiDjUid = downloadJobUid,
                djiUrl = manifestUrl.resolve(resource.href),
                djiTotalSize = (resource.size ?: 0).toLong()
            )
        } ?: emptyList()
        //Create the locks
    }

}