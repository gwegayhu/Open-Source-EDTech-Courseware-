package world.respect.shared.navigation

import androidx.navigation.NavHostController

/**
 * Wrapper that avoids accidental 'replay' of navigation commands.
 */
class RespectComposeNavController(
    private val navHostController: NavHostController,
) {
    @Volatile
    private var lastNavCommandTime = System.currentTimeMillis()

    fun onCollectNavCommand(
        navCommand: NavCommand,
    ) {
        if (navCommand.timestamp <= lastNavCommandTime) return

        when (navCommand) {
            is NavCommand.Navigate -> {
                lastNavCommandTime = navCommand.timestamp
                when (navCommand.destination) {
                    "back" -> navHostController.popBackStack()
                    else -> navHostController.navigate(navCommand.destination)
                }
            }
            is NavCommand.NavigateAndClearBackStack -> {
                lastNavCommandTime = navCommand.timestamp
                when (navCommand.destination) {
                    "back" -> navHostController.popBackStack()
                    else -> navHostController.navigate(navCommand.destination) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    fun navigate(destination: Any) {
        navHostController.navigate(destination)
    }

    fun popBackStack() {
        navHostController.popBackStack()
    }

    fun navigateUp() {
        navHostController.navigateUp()
    }
}