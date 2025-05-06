package world.respect.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import world.respect.app.screens.AppLauncherScreen
import world.respect.app.view.AssignmentScreen
import world.respect.app.view.ClazzScreen
import world.respect.app.view.ReportScreen
import world.respect.app.viewmodel.AppLauncherScreenViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppLauncher) {
        composable<AppLauncher> {
            AppLauncherScreen(viewModel = AppLauncherScreenViewModel(), navController = navController)
        }
        composable<Assignment> {
            AssignmentScreen()
        }
        composable<Clazz> { ClazzScreen() }
        composable<Report> { ReportScreen() }
    }
}



