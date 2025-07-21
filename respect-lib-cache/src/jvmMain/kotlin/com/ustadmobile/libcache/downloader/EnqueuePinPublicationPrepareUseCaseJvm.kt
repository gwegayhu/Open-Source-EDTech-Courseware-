package com.ustadmobile.libcache.downloader

import com.ustadmobile.libcache.db.UstadCacheDb
import com.ustadmobile.libcache.db.entities.DownloadJob
import io.ktor.http.Url

/**
 * Currently used only in JVM testing. In future can be used with Quartz scheduler (as WorkManager
 * is used on Android)
 */
class EnqueuePinPublicationPrepareUseCaseJvm(
    cacheDb: UstadCacheDb
) : AbstractEnqueuePinPublicationPrepareUseCase(cacheDb){

    override suspend fun invoke(manifestUrl: Url): DownloadJob {
        return createTransferJob(manifestUrl)
    }

}