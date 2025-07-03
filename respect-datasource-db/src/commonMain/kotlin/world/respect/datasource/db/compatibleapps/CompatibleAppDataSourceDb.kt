package world.respect.datasource.db.compatibleapps

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import world.respect.datasource.DataLoadMetaInfo
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataLoadState
import world.respect.datasource.LoadingStatus
import world.respect.datasource.compatibleapps.CompatibleAppsDataSourceLocal
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.datasource.db.RespectDatabase
import world.respect.datasource.db.adapters.asCompatibleAppEntity
import world.respect.datasource.db.adapters.asRespectManifestLoadResult

class CompatibleAppDataSourceDb(
    private val respectDb: RespectDatabase,
    private val json: Json,
): CompatibleAppsDataSourceLocal {

    override suspend fun upsertCompatibleApps(apps: List<DataLoadResult<RespectAppManifest>>) {
        respectDb.getCompatibleAppEntityDao().upsert(
            apps.mapNotNull { it.asCompatibleAppEntity(json) }
        )
    }

    override fun getApp(
        manifestUrl: String,
        loadParams: DataLoadParams
    ): Flow<DataLoadState<RespectAppManifest>> {
        return emptyFlow()
    }

    override fun getAddableApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<DataLoadState<RespectAppManifest>>>> {
        return respectDb.getCompatibleAppEntityDao().selectAllAsFlow().map { list ->
            DataLoadResult(
                data = list.map { it.asRespectManifestLoadResult(json) },
                metaInfo = DataLoadMetaInfo(
                    status = LoadingStatus.LOADED,
                    lastModified = list.maxOfOrNull { it.caeLastModified } ?: -1,
                )
            )
        }
    }

    override fun getLaunchpadApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<DataLoadState<RespectAppManifest>>>> {
        return emptyFlow()
    }

    override suspend fun addAppToLaunchpad(manifestUrl: String) {
        //Do nothing yet
    }

    override suspend fun removeAppFromLaunchpad(manifestUrl: String) {
        //Do nothing yet
    }
}