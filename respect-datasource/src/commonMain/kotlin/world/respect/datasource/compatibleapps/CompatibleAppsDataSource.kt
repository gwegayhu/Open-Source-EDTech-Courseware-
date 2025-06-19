package world.respect.datasource.compatibleapps

import kotlinx.coroutines.flow.Flow
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataResult
import world.respect.datasource.compatibleapps.model.RespectAppManifest

interface CompatibleAppsDataSource {

    /**
     * Load a specific app manifest
     */
    fun getApp(
        manifestUrl: String,
        loadParams: DataLoadParams
    ): Flow<DataResult<RespectAppManifest>>

    /**
     *
     */
    fun getAddableApps(
        loadParams: DataLoadParams
    ): Flow<DataResult<RespectAppManifest>>

    fun getLaunchpadApps(
        loadParams: DataLoadParams
    ): Flow<DataResult<List<RespectAppManifest>>>

    suspend fun addAppToLaunchpad(
        manifestUrl: String,
    )

    suspend fun removeAppFromLaunchpad(
        manifestUrl: String
    )

}
