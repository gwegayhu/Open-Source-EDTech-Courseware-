package world.respect.shared.viewmodel.clazz

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.update
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.clazz
import world.respect.shared.util.ext.asUiText

class ClazzViewModel(
    savedStateHandle: SavedStateHandle
): RespectViewModel(savedStateHandle) {
    init {
        _appUiState.update {
            it.copy(
                title = Res.string.clazz.asUiText(),
            )
        }
    }

}