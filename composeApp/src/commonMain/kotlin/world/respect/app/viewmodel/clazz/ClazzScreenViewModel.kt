package world.respect.app.viewmodel.clazz

import kotlinx.coroutines.flow.update
import world.respect.app.viewmodel.RespectViewModel

class ClazzScreenViewModel: RespectViewModel() {
    init {
        _appUiState.update {
            it.copy(
                title="Class",
                showBackButton = false,
            )
        }
    }

}