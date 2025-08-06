package world.respect.server.account.invite

import world.respect.datalayer.RespectAppDataSource
import world.respect.datalayer.respect.model.invite.RespectInviteInfo
import world.respect.libxxhash.XXStringHasher
import world.respect.shared.domain.account.invite.GetInviteInfoUseCase

class GetInviteInfoUseCaseServer(
    private val respectAppDataSource: RespectAppDataSource,
    private val xxStringHasher: XXStringHasher,
): GetInviteInfoUseCase {


    override suspend fun invoke(code: String): RespectInviteInfo {
        //Verify that this server handles the code prefix
        //Find the realm that handles the next prefix

        TODO("Not yet implemented")
    }
}