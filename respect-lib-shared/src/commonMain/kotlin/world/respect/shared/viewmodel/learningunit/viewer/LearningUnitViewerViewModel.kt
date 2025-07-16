package world.respect.shared.viewmodel.learningunit.viewer

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import world.respect.shared.navigation.LearningUnitViewer
import world.respect.shared.viewmodel.RespectViewModel

data class LearningUnitViewerViewModelUiState(
    val url: Url,
)

class LearningUnitViewerViewModel(
    savedStateHandle: SavedStateHandle,
): RespectViewModel(savedStateHandle) {

    val route: LearningUnitViewer = savedStateHandle.toRoute<LearningUnitViewer>()

    val uiState = MutableStateFlow(
        LearningUnitViewerViewModelUiState(
            url = route.learningUnitId,
        ),
    )

    init {
        _appUiState.update {
            it.copy(
                title = "",
                navigationVisible = false,
            )
        }
    }

}