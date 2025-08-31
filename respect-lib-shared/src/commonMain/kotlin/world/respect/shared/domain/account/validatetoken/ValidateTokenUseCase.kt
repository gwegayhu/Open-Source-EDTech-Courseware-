package world.respect.shared.domain.account.validatetoken

import world.respect.datalayer.AuthenticatedUserPrincipalId

interface ValidateTokenUseCase {

    suspend operator fun invoke(
        token: String
    ): AuthenticatedUserPrincipalId?

}