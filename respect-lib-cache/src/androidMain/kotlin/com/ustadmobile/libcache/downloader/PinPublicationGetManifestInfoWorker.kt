package com.ustadmobile.libcache.downloader

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class PinPublicationGetManifestInfoWorker(
    appContext: Context,
    params: WorkerParameters
) :CoroutineWorker(appContext, params) {



    override suspend fun doWork(): Result {
        return Result.success()
    }
}