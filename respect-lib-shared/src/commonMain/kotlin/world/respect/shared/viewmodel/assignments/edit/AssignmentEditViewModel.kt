package world.respect.shared.viewmodel.assignments.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.datalayer.dclazz.DClazzDataSource
import world.respect.datalayer.dclazz.model.DAssignment
import world.respect.shared.navigation.AssignmentDetail
import world.respect.shared.navigation.AssignmentEdit
import world.respect.shared.navigation.NavCommand
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.ActionBarButtonUiState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class AssignmentEditUiState(
    val assignment: DAssignment = DAssignment(),
)

@OptIn(ExperimentalTime::class)
class AssignmentEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val dClazzDataSource: DClazzDataSource,
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
        viewModelScope.launch {
            val assignment = _uiState.value.assignment
            dClazzDataSource.putAssignment(assignment)
            _navCommandFlow.tryEmit(
                NavCommand.Navigate(
                    destination = AssignmentDetail(
                        assignmentId = assignment.assignmentId
                    )
                )
            )
        }
    }

}