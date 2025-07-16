package world.respect.shared.viewmodel.learningunit.viewer

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.loading
import world.respect.shared.navigation.LearningUnitViewer
import world.respect.shared.resources.StringResourceUiText
import world.respect.shared.resources.UiText
import world.respect.shared.viewmodel.RespectViewModel

data class LearningUnitViewerViewModelUiState(
    val url: Url,
    val title: UiText,
)

class LearningUnitViewerViewModel(
    savedStateHandle: SavedStateHandle,
): RespectViewModel(savedStateHandle) {

    val route: LearningUnitViewer = savedStateHandle.toRoute<LearningUnitViewer>()

    val uiState = MutableStateFlow(
        LearningUnitViewerViewModelUiState(
            url = route.learningUnitId,
            title = StringResourceUiText(Res.string.loading)
        ),
    )

    init {

    }

}