package com.ustadmobile.libcache

import android.content.Context
import androidx.room.Room
import com.ustadmobile.libcache.db.AddNewEntryTriggerCallback
import com.ustadmobile.libcache.db.UstadCacheDb
import com.ustadmobile.libcache.logging.UstadCacheLogger
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import world.respect.libxxhash.XXStringHasher
import world.respect.libxxhash.jvmimpl.XXStringHasherCommonJvm

@Suppress("MemberVisibilityCanBePrivate")
class UstadCacheBuilder(
    var appContext: Context,
    var storagePath: Path,
    var dbName: String = DEFAULT_DB_NAME,
    var db: UstadCacheDb? = null,
    var logger: UstadCacheLogger? = null,
    var sizeLimit: () -> Long,
    var xxStringHasher: XXStringHasher = XXStringHasherCommonJvm(),
    var cachePathsProvider: CachePathsProvider = CachePathsProvider {
        CachePaths(
            tmpWorkPath = Path(storagePath, DEFAULT_SUBPATH_WORK),
            persistentPath = Path(storagePath, DEFAULT_SUBPATH_PERSISTENT),
            cachePath = Path(appContext.cacheDir.absolutePath, DEFAULT_SUBPATH_CACHE),
        )
    }
) {

    fun build(): UstadCache {
        return UstadCacheImpl(
            fileSystem = SystemFileSystem,
            pathsProvider = cachePathsProvider,
            logger =  logger,
            sizeLimit = sizeLimit,
            xxStringHasher = xxStringHasher,
            db = db ?: Room.databaseBuilder<UstadCacheDb>(appContext, dbName)
                .addCallback(AddNewEntryTriggerCallback())
                .addCallback(AddNewEntryTriggerCallback())
                .build()
        )
    }

    companion object {

        const val DEFAULT_SUBPATH_WORK = "tmpwork"

        const val DEFAULT_SUBPATH_PERSISTENT = "persistent"

        const val DEFAULT_SUBPATH_CACHE = "ustad-cache"

        const val DEFAULT_DB_NAME = "UstadCache"

    }

}