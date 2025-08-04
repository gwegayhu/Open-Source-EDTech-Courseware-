package world.respect.datalayer.directory

import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.datalayer.respect.model.invite.RespectInviteInfo

/**
 * DataSource to access all known directories
 */
interface RespectDirectoryDataSource {

    suspend fun allRealmsAsFlow(): Flow<DataLoadState<List<RespectRealm>>>

    suspend fun allRealmsInDirectory(): List<RespectRealm>

    suspend fun searchRealms(text: String): Flow<DataLoadState<List<RespectRealm>>>

    suspend fun getInviteInfo(inviteCode: String): RespectInviteInfo

}