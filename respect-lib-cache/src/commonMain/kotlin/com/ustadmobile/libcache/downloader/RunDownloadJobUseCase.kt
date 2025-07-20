package com.ustadmobile.libcache.downloader

interface RunDownloadJobUseCase {

    operator fun invoke(uid: Long)

}