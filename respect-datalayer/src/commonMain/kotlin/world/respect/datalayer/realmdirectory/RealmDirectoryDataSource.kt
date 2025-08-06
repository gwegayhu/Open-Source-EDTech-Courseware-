package world.respect.datalayer.realmdirectory

import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.datalayer.respect.model.RespectRealmDirectory
import world.respect.datalayer.respect.model.invite.RespectInviteInfo

/**
 * DataSource to access all known directories
 */
interface RealmDirectoryDataSource {

    suspend fun allDirectories(): List<RespectRealmDirectory>

    suspend fun allRealmsInDirectory(): List<RespectRealm>

    suspend fun searchRealms(text: String): Flow<DataLoadState<List<RespectRealm>>>

    suspend fun getInviteInfo(inviteCode: String): RespectInviteInfo

}