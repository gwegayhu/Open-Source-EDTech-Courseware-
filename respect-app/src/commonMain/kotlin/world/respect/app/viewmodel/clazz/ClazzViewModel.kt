package world.respect.app.viewmodel.clazz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.app.viewmodel.RespectViewModel
import org.jetbrains.compose.resources.getString
import respect.respect_app.generated.resources.Res
import respect.respect_app.generated.resources.clazz

class ClazzViewModel(
    savedStateHandle: SavedStateHandle
): RespectViewModel(savedStateHandle) {
    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(
                    title = getString(resource = Res.string.clazz),
                    showBackButton = false,
                )
            }
        }
    }

}