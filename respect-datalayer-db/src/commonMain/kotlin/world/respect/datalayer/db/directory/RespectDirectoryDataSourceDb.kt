package world.respect.datalayer.db.directory

import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.directory.RespectDirectoryDataSourceLocal
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.datalayer.respect.model.RespectRealmDirectory
import world.respect.datalayer.respect.model.invite.RespectInviteInfo

class RespectDirectoryDataSourceDb: RespectDirectoryDataSourceLocal {

    override suspend fun allDirectories(): List<RespectRealmDirectory> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertRealm(realm: RespectRealm) {
        TODO("Not yet implemented")
    }

    override suspend fun allRealmsInDirectory(): List<RespectRealm> {
        TODO("Not yet implemented")
    }

    override suspend fun searchRealms(text: String): Flow<DataLoadState<List<RespectRealm>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getInviteInfo(inviteCode: String): RespectInviteInfo {
        TODO("Not yet implemented")
    }
}