package world.respect.shared.navigation

import world.respect.shared.util.systemTimeInMillis

sealed class NavCommand(
    val timestamp: Long = systemTimeInMillis(),
) {
    class Navigate(
        val destination: RespectAppRoute,
        val clearBackStack: Boolean = false,
        timestamp: Long = systemTimeInMillis(),
    ) : NavCommand(timestamp)

    class Pop(
        val destination: RespectAppRoute,
        val inclusive: Boolean,
        timestamp: Long = systemTimeInMillis(),
    ): NavCommand(timestamp)

}

