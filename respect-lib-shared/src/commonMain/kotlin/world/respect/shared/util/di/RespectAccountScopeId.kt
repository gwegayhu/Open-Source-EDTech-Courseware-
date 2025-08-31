package world.respect.shared.util.di

import io.ktor.http.Url
import world.respect.datalayer.AuthenticatedUserPrincipalId

/**
 * Data class that handles Koin scope id for Respect account scope.
 *
 * @property schoolUrl the SchoolUrl as per SchoolDirectoryEntry.self
 * @property accountPrincipalId the authenticated user principal id as per Person.guid
 */
data class RespectAccountScopeId(
    val schoolUrl: Url,
    val accountPrincipalId: AuthenticatedUserPrincipalId,
) {

    val scopeId = "a:${accountPrincipalId}@${schoolUrl}"

    companion object {

        fun parse(scopeId: String): RespectAccountScopeId {
            return scopeId.substringAfter(":").let {
                val atIndex = it.lastIndexOf('@')
                RespectAccountScopeId(
                    schoolUrl = Url(it.substring(0, atIndex)),
                    accountPrincipalId = AuthenticatedUserPrincipalId(it.substring(atIndex + 1))
                )
            }
        }

    }
}