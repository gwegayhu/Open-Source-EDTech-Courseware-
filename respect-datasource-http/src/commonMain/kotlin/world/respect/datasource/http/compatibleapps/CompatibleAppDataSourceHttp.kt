package world.respect.datasource.http.compatibleapps

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import world.respect.datasource.DataErrorResult
import world.respect.datasource.DataLoadMetaInfo
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataReadyState
import world.respect.datasource.DataLoadState
import world.respect.datasource.DataLoadingState
import world.respect.datasource.compatibleapps.CompatibleAppsDataSource
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.datasource.ext.getAsDataLoadState
import world.respect.datasource.ext.getDataLoadResultAsFlow
import world.respect.libutil.ext.resolve


class CompatibleAppDataSourceHttp(
    private val httpClient: HttpClient,
    defaultCompatibleAppListUrl: String,
): CompatibleAppsDataSource {

    private val defaultCompatibleAppListUrlObj = Url(defaultCompatibleAppListUrl)

    override suspend fun getApp(
        manifestUrl: Url,
        loadParams: DataLoadParams,
    ): DataLoadState<RespectAppManifest> {
        return httpClient.getAsDataLoadState(manifestUrl)
    }

    override fun getAppAsFlow(
        manifestUrl: Url,
        loadParams: DataLoadParams,
    ): Flow<DataLoadState<RespectAppManifest>> {
        return httpClient.getDataLoadResultAsFlow(
            manifestUrl, loadParams
        )
    }

    override fun getAddableApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<DataLoadState<RespectAppManifest>>>> {
        return flow {
            emit(DataLoadingState())
            try {
                val respectAppUrls: List<String> = httpClient.get(defaultCompatibleAppListUrlObj)
                    .body()
                val manifests = respectAppUrls.map { manifestHref ->
                    httpClient.getAsDataLoadState<RespectAppManifest>(
                        defaultCompatibleAppListUrlObj.resolve(manifestHref)
                    )
                }

                emit(
                    DataReadyState(
                        data = manifests,
                        metaInfo = DataLoadMetaInfo(
                            lastModified = manifests.maxOfOrNull { it.metaInfo.lastModified } ?: -1L
                        ),
                    )
                )
            }catch(e: Throwable) {
                emit(DataErrorResult(e))
            }
        }
    }

    override fun getLaunchpadApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<DataLoadState<RespectAppManifest>>>> {
        return emptyFlow()
    }

    override suspend fun addAppToLaunchpad(manifestUrl: Url) {
        //do nothing - does not run remotely
    }

    override suspend fun removeAppFromLaunchpad(manifestUrl: Url) {
        //do nothing - does not run remotely
    }

}