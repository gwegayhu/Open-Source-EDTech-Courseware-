package world.respect.datasource.compatibleapps

import kotlinx.coroutines.flow.Flow
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadState
import world.respect.datasource.compatibleapps.model.RespectAppManifest

interface CompatibleAppsDataSource {

    /**
     * Load a specific app manifest
     */
    fun getApp(
        manifestUrl: String,
        loadParams: DataLoadParams
    ): Flow<DataLoadState<RespectAppManifest>>

    /**
     *
     */
    fun getAddableApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<RespectAppManifest>>>

    fun getLaunchpadApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<RespectAppManifest>>>

    suspend fun addAppToLaunchpad(
        manifestUrl: String,
    )

    suspend fun removeAppFromLaunchpad(
        manifestUrl: String
    )

}
