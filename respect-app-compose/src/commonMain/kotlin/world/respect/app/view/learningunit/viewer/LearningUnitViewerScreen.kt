package world.respect.app.view.learningunit.viewer

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import world.respect.shared.viewmodel.learningunit.viewer.LearningUnitViewerViewModel
import world.respect.shared.viewmodel.learningunit.viewer.LearningUnitViewerViewModelUiState

@Composable
fun LearningUnitViewerScreen(
    viewModel: LearningUnitViewerViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    LearningUnitViewerScreen(uiState)
}

@Composable
fun LearningUnitViewerScreen(
    uiState: LearningUnitViewerViewModelUiState,
) {
    Text(uiState.url.toString())
}

