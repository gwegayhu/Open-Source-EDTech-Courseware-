package world.respect.shared.domain.account.validateauth

import world.respect.datalayer.db.RespectSchoolDatabase
import world.respect.shared.domain.AuthenticatedUserPrincipalId
import world.respect.shared.util.systemTimeInMillis

class ValidateAuthorizationUseCaseDbImpl(
    private val schoolDb: RespectSchoolDatabase,
): ValidateAuthorizationUseCase {

    override suspend fun invoke(
        credential: ValidateAuthorizationUseCase.AuthorizationCredential
    ): AuthenticatedUserPrincipalId {
        when(credential) {
            is ValidateAuthorizationUseCase.BearerTokenCredential -> {
                val dbToken = schoolDb.getAuthTokenEntityDao().findByToken(
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