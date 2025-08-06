package world.respect.datalayer.respect.model

import io.ktor.http.Url
import kotlinx.serialization.Serializable

/**
 * A RESPECT Realm Directory is a base URL that:
 * Implements RESPECT directory APIs (e.g. invite management) for the given invitePrefix
 * Provides a list of Respect Realms
 *
 * @property invitePrefix the prefix for invites that are managed by this directory
 * @property baseUrl the URL of this directory server e.g. https://respect.example.org/api/respect-directory/
 *           On the server side, there is always one realm with the baseUrl set as
 *           SERVER_MANAGED_DIRECTORY_URL.
 */
@Serializable
data class RespectRealmDirectory(
    val invitePrefix: String,
    val baseUrl: Url,
) {

    companion object {

        const val SERVER_MANAGED_DIRECTORY_URL = "local-dir://localhost/"

    }

}
