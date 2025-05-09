package world.respect.app.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import world.respect.app.view.applauncher.AppLauncherScreen
import world.respect.app.view.applist.AppListScreen
import world.respect.app.view.appsdetail.AppsDetailScreen
import world.respect.app.view.assignments.AssignmentScreen
import world.respect.app.view.clazz.ClazzScreen
import world.respect.app.view.enterlink.EnterLinkScreen
import world.respect.app.view.report.ReportScreen
import world.respect.app.appstate.AppUiState

@Composable
fun AppNavHost(
    navController: NavHostController,
    onSetAppUiState: (AppUiState) -> Unit,
    modifier: Modifier,
) {

    NavHost(navController = navController, startDestination = AppLauncher, modifier = modifier) {
        composable<AppLauncher> {
            AppLauncherScreen(
                navController = navController,
            )
        }
        composable<Assignment> { AssignmentScreen() }
        composable<Clazz> { ClazzScreen() }
        composable<Report> { ReportScreen() }
        composable<AppList> { AppListScreen(navController = navController) }
        composable<EnterLink> { EnterLinkScreen() }
        composable<AppsDetail> { AppsDetailScreen() }

    }
}



