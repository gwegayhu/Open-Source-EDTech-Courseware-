package world.respect.datalayer.http.realm

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import world.respect.datalayer.DataErrorResult
import world.respect.datalayer.DataLoadMetaInfo
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataLoadingState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSource
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.datalayer.respect.model.RespectRealmDirectory
import world.respect.datalayer.respect.model.invite.RespectInviteInfo
import world.respect.libutil.ext.resolve
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class RealmDirectoryDataSourceHttp(
    private val httpClient: HttpClient,
    private val defaultDirectoryUrl: String
    ) : RealmDirectoryDataSource {
    private val defaultDirectoryUrlObj = Url(defaultDirectoryUrl)


    override suspend fun allDirectories(): List<RespectRealmDirectory> {
        TODO("Not yet implemented")
    }

    override suspend fun allRealmsInDirectory(): List<RespectRealm> {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun searchRealms(text: String): Flow<DataLoadState<List<RespectRealm>>> {
        return flow {
            emit(DataLoadingState())
            try {
                val url = defaultDirectoryUrlObj.resolve("/api/directory/realms?q=$text")
                val realms: List<RespectRealm> = httpClient.get(url).body()
                emit(
                    DataReadyState(
                        data = realms,
                        metaInfo = DataLoadMetaInfo(
                            lastModified = Clock.System.now().toEpochMilliseconds()
                        )
                    )
                )
            } catch (e: Throwable) {
                emit(DataErrorResult(e))
            }
        }
    }
    override suspend fun getInviteInfo(inviteCode: String): RespectInviteInfo {
        TODO("Not yet implemented")
    }

    override suspend fun getRealmByUrl(url: Url): DataReadyState<RespectRealm>? {
        TODO("Not yet implemented")
    }
}
