package world.respect.datasource.repository.compatibleapps

import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataLoadState
import world.respect.datasource.compatibleapps.CompatibleAppsDataSource
import world.respect.datasource.compatibleapps.CompatibleAppsDataSourceLocal
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.datasource.repository.ext.checkIsRemoteUpdated
import world.respect.datasource.repository.ext.combineLocalWithRemote
import world.respect.datasource.repository.ext.copyLoadState

class CompatibleAppDataSourceRepository(
    private val local: CompatibleAppsDataSourceLocal,
    private val remote: CompatibleAppsDataSource,
) : CompatibleAppsDataSource{

    override suspend fun getApp(
        manifestUrl: Url,
        loadParams: DataLoadParams
    ): DataLoadState<RespectAppManifest> {
        val localResult = local.getApp(manifestUrl, loadParams)
        val remoteResult = remote.getApp(manifestUrl, loadParams)

        return localResult.checkIsRemoteUpdated(remoteResult).updatedRemoteData?.also { updatedData ->
            local.upsertCompatibleApps(listOf(updatedData))
            remoteResult
        } ?: localResult.copyLoadState(
            localMetaInfo = localResult.metaInfo,
            remoteMetaInfo = remoteResult.metaInfo
        )
    }

    override fun getAppAsFlow(
        manifestUrl: String,
        loadParams: DataLoadParams
    ): Flow<DataLoadState<RespectAppManifest>> {
        return local.getAppAsFlow(manifestUrl, loadParams).combineLocalWithRemote(
            remoteFlow = remote.getAppAsFlow(manifestUrl, loadParams),
            onRemoteNewer = { newApp ->
                local.upsertCompatibleApps(listOf(newApp))
            }
        )
    }

    override fun getAddableApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<DataLoadState<RespectAppManifest>>>> {
        return local.getAddableApps(loadParams).combineLocalWithRemote(
            remoteFlow = remote.getAddableApps(loadParams),
            onRemoteNewer = { newList ->
                local.upsertCompatibleApps(
                    newList.data?.mapNotNull { it as? DataLoadResult } ?: emptyList()
                )
            }
        )
    }

    override fun getLaunchpadApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<DataLoadState<RespectAppManifest>>>> {
        return emptyFlow()
    }

    override suspend fun addAppToLaunchpad(manifestUrl: String) {

    }

    override suspend fun removeAppFromLaunchpad(manifestUrl: String) {

    }
}