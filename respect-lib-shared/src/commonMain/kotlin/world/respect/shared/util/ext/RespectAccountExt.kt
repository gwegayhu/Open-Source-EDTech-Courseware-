package world.respect.shared.util.ext

import world.respect.shared.domain.account.RespectAccount

/**
 * Returns true if the two accounts are the same account - the same user guid on the same realm
 */
fun RespectAccount.isSameAccount(other: RespectAccount): Boolean {
    return other.realm.self == realm.self && other.userSourcedId == userSourcedId
}
