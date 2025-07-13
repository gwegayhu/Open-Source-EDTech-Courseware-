package world.respect.app.viewmodel.assignments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import respect.respect_app.generated.resources.Res
import respect.respect_app.generated.resources.assignments
import world.respect.app.viewmodel.RespectViewModel

class AssignmentViewModel(
    savedStateHandle: SavedStateHandle
) : RespectViewModel(savedStateHandle) {
    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(
                    title = getString(resource = Res.string.assignments),
                    showBackButton = false,
                )
            }
        }
    }
}