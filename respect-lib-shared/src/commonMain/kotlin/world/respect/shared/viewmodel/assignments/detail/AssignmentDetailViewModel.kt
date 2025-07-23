package world.respect.shared.viewmodel.assignments.detail

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
import world.respect.shared.viewmodel.RespectViewModel

data class AssignmentDetailUiState(
    val assignment: DAssignment? = null,
)

class AssignmentDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val dClazzDataSource: DClazzDataSource,
): RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(AssignmentDetailUiState())

    val uiState = _uiState.asStateFlow()

    private val route: AssignmentDetail = savedStateHandle.toRoute()

    init {
        viewModelScope.launch {
            dClazzDataSource.getAssignmentAsFlow(
                route.assignmentId
            ).collect { assignment ->
                _uiState.update { prev ->
                    prev.copy(assignment = assignment)
                }

                _appUiState.update { prev ->
                    prev.copy(
                        title = assignment?.title ?: "",
                    )
                }
            }
        }
    }

}