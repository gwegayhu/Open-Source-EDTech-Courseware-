package world.respect.datasource.http.compatibleapps

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataLoadingState
import world.respect.datasource.DataLoadState
import world.respect.datasource.LoadingStatus
import world.respect.datasource.compatibleapps.CompatibleAppsDataSource
import world.respect.datasource.compatibleapps.model.RespectAppManifest

class CompatibleAppDataSourceHttp(
    private val httpClient: HttpClient,
    private val defaultCompatibleAppListUrl: String,
): CompatibleAppsDataSource {

    override fun getApp(
        manifestUrl: String,
        loadParams: DataLoadParams,
    ): Flow<DataLoadState<RespectAppManifest>> {
        return flow {
            emit(DataLoadingState())
            val loadUrl = Url(manifestUrl)
            val result: RespectAppManifest = httpClient.get(loadUrl).body()
            emit(DataLoadResult(data = result.copy(selfUrl = loadUrl)))
        }
    }

    override fun getAddableApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<RespectAppManifest>>> {
        return flow {
            emit(DataLoadingState())
            val respectAppUrls: List<String> = httpClient.get(defaultCompatibleAppListUrl).body()
            val manifests = respectAppUrls.mapNotNull { url ->
                try {
                    httpClient.get(url).body<RespectAppManifest>().copy(
                        selfUrl = Url(url),
                    )
                }catch(e: Throwable) {
                    //Log
                    null
                }
            }
            emit(DataLoadResult(data = manifests))
        }
    }

    override fun getLaunchpadApps(loadParams: DataLoadParams): Flow<DataLoadState<List<RespectAppManifest>>> {
        return emptyFlow()
    }

    override suspend fun addAppToLaunchpad(manifestUrl: String) {
        //do nothing - does not run remotely
    }

    override suspend fun removeAppFromLaunchpad(manifestUrl: String) {
        //do nothing - does not run remotely
    }

}