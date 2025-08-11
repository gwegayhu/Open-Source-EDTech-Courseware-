package world.respect.server.domain.account.validateauth

import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.shared.domain.AuthenticatedUserPrincipalId
import world.respect.shared.domain.account.validateauth.ValidateAuthorizationUseCase
import world.respect.shared.util.systemTimeInMillis

class ValidateAuthorizationUseCaseDbImpl(
    private val realmDb: RespectRealmDatabase,
): ValidateAuthorizationUseCase {

    override suspend fun invoke(
        credential: ValidateAuthorizationUseCase.AuthorizationCredential
    ): AuthenticatedUserPrincipalId {
        when(credential) {
            is ValidateAuthorizationUseCase.BearerTokenCredential -> {
                val dbToken = realmDb.getAuthTokenEntityDao().findByToken(
                    credential.token, systemTimeInMillis(),
                ) ?: throw IllegalArgumentException()

                return AuthenticatedUserPrincipalId(dbToken.atPGuid)
            }

            else -> {
                throw IllegalArgumentException()
            }
        }

    }
}