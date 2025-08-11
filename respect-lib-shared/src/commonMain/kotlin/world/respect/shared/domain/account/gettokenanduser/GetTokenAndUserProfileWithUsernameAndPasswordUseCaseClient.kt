package world.respect.shared.domain.account.gettokenanduser

import io.ktor.http.Url
import world.respect.shared.domain.account.AuthResponse

class GetTokenAndUserProfileWithUsernameAndPasswordUseCaseClient(
    private val realmUrl: Url,
): GetTokenAndUserProfileWithUsernameAndPasswordUseCase {

    override suspend fun invoke(
        username: String,
        password: String
    ): AuthResponse {
        TODO("Make http request")
    }
}