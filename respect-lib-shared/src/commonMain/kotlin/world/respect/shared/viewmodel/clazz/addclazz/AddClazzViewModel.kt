package world.respect.shared.viewmodel.clazz.addclazz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.save
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.ActionBarButtonUiState

class AddClazzViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    init {
        viewModelScope.launch {
            _appUiState.update { prev ->
                prev.copy(
                    userAccountIconVisible=false,
                    actionBarButtonState = ActionBarButtonUiState(
                        visible = true,
                        text = getString(Res.string.save)
                    ),
                )
            }
        }
    }
}