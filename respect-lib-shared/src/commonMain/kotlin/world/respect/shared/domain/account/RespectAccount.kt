package world.respect.shared.domain.account

import kotlinx.serialization.Serializable
import world.respect.datalayer.respect.model.RespectRealm

/**
 * Placeholder representing a user account (likely linked to an upstream xAPI and OneRoster server)
 *
 * The RESPECT Account Manager can provide a Koin Scope for a given account.
 */
@Serializable
data class RespectAccount(
    val userSourcedId: String,
    val realm: RespectRealm,
) {

    /**
     * The ScopeId to use for dependency injection - always the userSourcedId@realmUrl
     */
    val scopeId: String
        get() = "$userSourcedId@${realm.self}"

}
