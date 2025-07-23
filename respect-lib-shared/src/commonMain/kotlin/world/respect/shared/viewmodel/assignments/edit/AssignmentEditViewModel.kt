package world.respect.shared.viewmodel.assignments.edit

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import world.respect.datalayer.dclazz.model.DAssignment
import world.respect.shared.navigation.AssignmentEdit
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.ActionBarButtonUiState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class AssignmentEditUiState(
    val assignment: DAssignment = DAssignment(),
)

@OptIn(ExperimentalTime::class)
class AssignmentEditViewModel(
    savedStateHandle: SavedStateHandle
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(AssignmentEditUiState())

    val uiState = _uiState.asStateFlow()

    private val route: AssignmentEdit = savedStateHandle.toRoute()

    init {
        _appUiState.update {
            it.copy(
                title = "Edit Assignment",
                actionBarButtonState = ActionBarButtonUiState(
                    visible = true,
                    text = "Save",
                    enabled = true,
                    onClick = ::onClickSave,
                ),
                userAccountIconVisible = false,
            )
        }

        _uiState.update { prev ->
            prev.copy(
                assignment = prev.assignment.copy(
                    assignmentId = Clock.System.now().toEpochMilliseconds(),
                    toClazzId = route.assignToClazzId ?: "",
                    learningUnitId = route.lessonId ?: ""
                )
            )
        }
    }

    fun onEntityChanged(entity: DAssignment) {
        _uiState.update { prev ->
            prev.copy(
                assignment = entity
            )
        }
    }

    fun onClickSave() {

    }


}