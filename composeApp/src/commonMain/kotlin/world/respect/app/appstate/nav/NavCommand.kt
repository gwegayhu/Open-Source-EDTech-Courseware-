package world.respect.app.appstate.nav


/**
 * @param timestamp - this can be used to identify nav commands and record if they have been
 * processed.
 */
sealed class NavCommand(val timestamp: Long)

data class NavigateNavCommand(
    val viewName: String,
    val args: Map<String, String>,
): NavCommand(System.currentTimeMillis())

data class PopNavCommand(
    val viewName: String,
    val inclusive: Boolean
): NavCommand(System.currentTimeMillis())

class TestNavCommand(timestamp: Long): NavCommand(timestamp)

