package world.respect.datalayer.repository.realm

import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import world.respect.datalayer.DataLoadMetaInfo
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.ext.combineWithRemote
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSource
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSourceLocal
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.datalayer.respect.model.RespectRealmDirectory
import world.respect.datalayer.respect.model.invite.RespectInviteInfo
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class RealmDirectoryDataSourceRepository(
    private val local: RealmDirectoryDataSourceLocal,
    private val remote: RealmDirectoryDataSource,
): RealmDirectoryDataSource{
    override suspend fun allDirectories(): List<RespectRealmDirectory> {
        TODO("Not yet implemented")
    }

    override suspend fun allRealmsInDirectory(): List<RespectRealm> {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun searchRealms(text: String): Flow<DataLoadState<List<RespectRealm>>> {
        val remoteResults = remote.searchRealms(text).onEach { loadState ->
            if (loadState is DataReadyState) {
                loadState.data.forEach { realm ->
                    local.upsertRealm(
                        DataReadyState(
                            realm,
                            DataLoadMetaInfo(
                                lastModified = Clock.System.now().toEpochMilliseconds()
                            )
                        ),
                        directory = null
                    )
                }
            }
        }

        return local.searchRealms(text).combine(remoteResults) { localState, remoteState ->
            localState.combineWithRemote(remoteState)
        }
    }

    override suspend fun getInviteInfo(inviteCode: String): RespectInviteInfo {
        TODO("Not yet implemented")
    }

    override suspend fun getRealmByUrl(url: Url): DataReadyState<RespectRealm>? {
        TODO("Not yet implemented")
    }

}