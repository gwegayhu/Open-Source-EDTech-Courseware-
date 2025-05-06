package world.respect.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import world.respect.app.view.AppLauncherScreen
import world.respect.app.view.AssignmentScreen
import world.respect.app.view.ClazzScreen
import world.respect.app.view.ReportScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = AppLauncher,modifier = modifier) {
        composable<AppLauncher> {
            AppLauncherScreen()
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



