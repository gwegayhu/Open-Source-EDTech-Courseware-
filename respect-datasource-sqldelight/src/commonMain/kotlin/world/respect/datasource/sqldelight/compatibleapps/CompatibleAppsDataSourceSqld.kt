package world.respect.datasource.sqldelight.compatibleapps

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataLoadState
import world.respect.datasource.compatibleapps.CompatibleAppsDataSourceLocal
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.datasource.sqldelight.CompatibleAppEntityQueries
import kotlin.coroutines.CoroutineContext

class CompatibleAppsDataSourceSqld(
    private val queries: CompatibleAppEntityQueries,
    private val json: Json,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
): CompatibleAppsDataSourceLocal {

    override fun getApp(
        manifestUrl: String,
        loadParams: DataLoadParams
    ): Flow<DataLoadState<RespectAppManifest>> {
        return emptyFlow()
    }

    override suspend fun upsertCompatibleApps(apps: List<RespectAppManifest>) {
        withContext(coroutineContext) {
            queries.transaction {
                apps.forEach {
                    queries.upsert(it.asCompatibleAppEntity(json))
                }
            }
        }
    }

    override fun getAddableApps(loadParams: DataLoadParams): Flow<DataLoadState<List<RespectAppManifest>>> {
        return queries.selectAll().asFlow().mapToList(coroutineContext).map { resultList ->
            DataLoadResult(
                data = resultList.map { it.asRespectManifest(json) },
            )
        }
    }

    override fun getLaunchpadApps(loadParams: DataLoadParams): Flow<DataLoadState<List<RespectAppManifest>>> {
        return emptyFlow()
    }

    override suspend fun addAppToLaunchpad(manifestUrl: String) {

    }

    override suspend fun removeAppFromLaunchpad(manifestUrl: String) {

    }
}