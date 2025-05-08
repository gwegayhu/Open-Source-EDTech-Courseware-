package world.respect.app.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import respect.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import world.respect.app.viewmodel.ReportScreenViewModel
import respect.composeapp.generated.resources.report


@Composable
fun ReportScreen(viewModel: ReportScreenViewModel = viewModel { ReportScreenViewModel() },
) {
    Text(text=stringResource(Res.string.report))
}


