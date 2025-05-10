package world.respect.app.view.report

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import respect.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import world.respect.app.viewmodel.report.ReportScreenViewModel
import respect.composeapp.generated.resources.report


@Composable
fun ReportScreen(
    viewModel: ReportScreenViewModel = viewModel { ReportScreenViewModel() },
    navController: NavHostController,
) {
    Text(text=stringResource(Res.string.report))
}


