package world.respect.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import world.respect.app.screens.AppLauncherScreen
import world.respect.app.view.AssignmentScreen
import world.respect.app.view.ClazzScreen
import world.respect.app.view.ReportScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppLauncher) {
        composable<AppLauncher> {
            AppLauncherScreen(navController = navController)
        }
        composable<Assignment> {
            AssignmentScreen()
        }
        composable<Clazz> { ClazzScreen() }
        composable<Report> {
            ReportScreen()
        }
    }
}



