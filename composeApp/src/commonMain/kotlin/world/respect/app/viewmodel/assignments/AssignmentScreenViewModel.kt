package world.respect.app.viewmodel.assignments

import kotlinx.coroutines.flow.update
import world.respect.app.viewmodel.RespectViewModel

class AssignmentScreenViewModel() : RespectViewModel() {
    init {
        _appUiState.update {
            it.copy(
                title="Assignments",
                showBackButton = false,
                )
        }
    }
}