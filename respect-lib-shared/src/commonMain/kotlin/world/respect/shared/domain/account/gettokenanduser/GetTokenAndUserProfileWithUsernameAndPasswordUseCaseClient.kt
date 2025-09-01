package world.respect.shared.domain.account.gettokenanduser

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import world.respect.shared.domain.account.AuthResponse

class GetTokenAndUserProfileWithUsernameAndPasswordUseCaseClient(
    private val schoolUrl: Url,
    private val httpClient: HttpClient,
): GetTokenAndUserProfileWithUsernameAndPasswordUseCase {

    override suspend fun invoke(
        username: String,
        password: String
    ): AuthResponse {
        return httpClient.post {
            url {
                takeFrom(schoolUrl)
                appendPathSegments("api/school/respect-ext/auth/auth-with-password")
            }

            parameter(GetTokenAndUserProfileWithUsernameAndPasswordUseCase.PARAM_NAME_USERNAME, username)
            contentType(ContentType.Text.Plain)
            setBody(password)
        }.body()
    }
}