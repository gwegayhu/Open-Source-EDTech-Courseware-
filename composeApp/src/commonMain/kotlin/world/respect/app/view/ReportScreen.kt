package world.respect.app.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import kotlinx.coroutines.flow.Flow
import org.kodein.di.compose.localDI
import world.respect.app.appstate.AppUiState
import world.respect.app.viewmodel.ReportScreenViewModel

@Composable
fun ReportScreen() {
    val di = localDI()
    val viewModel = ReportScreenViewModel(di)
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.reportData.isEmpty()) {
        Text("Loading report...")
    } else {
        Text(text = uiState.reportTitle)
        uiState.reportData.forEach {
            Text(it)
        }
    }
}


