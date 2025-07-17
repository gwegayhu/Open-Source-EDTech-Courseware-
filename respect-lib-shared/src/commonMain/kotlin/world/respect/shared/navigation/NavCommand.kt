package world.respect.shared.navigation

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
sealed class NavCommand(
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
) {
    @OptIn(ExperimentalTime::class)
    class Navigate(
        val destination: Any,  // CHANGED: Use Any instead of RespectAppRoute
        timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    ): NavCommand(timestamp)

    @OptIn(ExperimentalTime::class)
    class NavigateAndClearBackStack(  // ADDED: Missing class
        val destination: Any,
        timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    ): NavCommand(timestamp)
}

