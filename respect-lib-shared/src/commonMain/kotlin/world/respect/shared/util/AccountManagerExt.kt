package world.respect.shared.util

import world.respect.shared.domain.account.RespectAccountManager

fun RespectAccountManager.isActiveUserTeacher(): Boolean {
    return activeAccount?.userSourcedId?.startsWith("teacher") == true
}