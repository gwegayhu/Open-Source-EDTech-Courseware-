package world.respect.shared.viewmodel.assignments.edit

import androidx.lifecycle.SavedStateHandle
import world.respect.shared.viewmodel.RespectViewModel

data class AssignmentListUiState(
    val title: String = ""
)

class AssignmentEditViewModel(
    savedStateHandle: SavedStateHandle
) : RespectViewModel(savedStateHandle) {


}