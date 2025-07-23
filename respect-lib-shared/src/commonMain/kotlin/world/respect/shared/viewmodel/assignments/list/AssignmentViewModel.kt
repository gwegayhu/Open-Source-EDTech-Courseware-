package world.respect.shared.viewmodel.assignments.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import world.respect.datalayer.dclazz.DClazzDataSource
import world.respect.datalayer.dclazz.model.DAssignment
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.assignments
import world.respect.shared.navigation.AssignmentDetail
import world.respect.shared.navigation.NavCommand
import world.respect.shared.viewmodel.RespectViewModel

data class AssignmentListUiState(
    val assignments: List<DAssignment> = emptyList()
)

class AssignmentViewModel(
    savedStateHandle: SavedStateHandle,
    dClazzDataSource: DClazzDataSource,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(AssignmentListUiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(
                    title = getString(resource = Res.string.assignments),
                    showBackButton = false,
                )
            }
        }

        viewModelScope.launch {
            dClazzDataSource.getAllAssignmentsAsFlow().collect { assignments ->
                _uiState.update { prev ->
                    prev.copy(assignments = assignments)
                }
            }
        }
    }

    fun onClickAssignment(assignment: DAssignment) {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                AssignmentDetail(
                    assignmentId =  assignment.assignmentId
                )
            )
        )
    }

}