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
import world.respect.app.viewmodel.applauncher.AppLauncherScreenViewModel
import world.respect.app.viewmodel.appsdetail.AppsDetailScreenViewModel
import world.respect.app.viewmodel.respectViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    onSetAppUiState: (AppUiState) -> Unit,
    modifier: Modifier,
) {


    NavHost(navController = navController, startDestination = AppLauncher, modifier = modifier) {
        composable<AppLauncher> {

            val viewModel = respectViewModel(
                modelClass = AppLauncherScreenViewModel::class,
                onSetAppUiState = onSetAppUiState,
                navController = navController
            )
            AppLauncherScreen(
                navController = navController,
                viewModel = viewModel,
                )
        }
        composable<Assignment> { AssignmentScreen() }
        composable<Clazz> { ClazzScreen() }
        composable<Report> { ReportScreen() }
        composable<AppList> { AppListScreen(navController = navController) }
        composable<EnterLink> { EnterLinkScreen() }
        composable<AppsDetail> {
            val viewModel = respectViewModel(
                modelClass = AppsDetailScreenViewModel::class,
                onSetAppUiState = onSetAppUiState,
                navController = navController
            )
            AppsDetailScreen(viewModel = viewModel)
        }

    }
}



