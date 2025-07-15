package world.respect.shared.directory

import io.ktor.http.Url
import kotlinx.serialization.Serializable

/**
 * @property directories a list of RespectDirectoryServer that implement the RESPECT Directory APIs
 */
@Serializable
data class RespectDirectoryServersConfig(
    val directories: List<RespectDirectoryServer>,
) {

    /**
     * A RESPECT Directory is a base URL that:
     *  a) Implements RESPECT directory APIs (e.g. invite management) for the given invitePrefix
     *  b) Is https-enabled and verified for use with deep links and passkeys (for *.domain)
     *  c) Has subdomains which are RESPECT Realms
     *
     * @property invitePrefix the prefix for invites that are managed by this directory
     * @property baseUrl the URL of this directory server e.g. https://respect.example.org/
     */
    @Serializable
    data class RespectDirectoryServer(
        val invitePrefix: String,
        val baseUrl: Url,
    )

}
