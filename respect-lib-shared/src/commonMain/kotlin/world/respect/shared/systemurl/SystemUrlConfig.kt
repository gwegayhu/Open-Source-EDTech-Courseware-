package world.respect.shared.systemurl

import io.ktor.http.Url
import kotlinx.serialization.Serializable

/**
 * @property baseUrls a list of System Base URLs that are verified (and can be used for links,
 * passkeys, invite handling, etc)
 */
@Serializable
class SystemUrlConfig(
    val baseUrls: List<SystemBaseUrl>,
) {

    /**
     * A System Base URL is a top level URL that:
     *  a) Implements the RESPECT invite API to handle invites with the given invitePrefix
     *  b) Is https-enabled and verified for use with deep links and passkeys (for *.domain)
     *  c) Has subdomains which implement RESPECT API endpoints
     */
    @Serializable
    class SystemBaseUrl(
        val invitePrefix: String,
        val baseUrl: Url,
    )

}
