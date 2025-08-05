package world.respect.shared.viewmodel.clazz.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.datalayer.oneroster.rostering.FakeRosterDataSource
import world.respect.datalayer.oneroster.rostering.model.OneRosterRoleEnum
import world.respect.datalayer.oneroster.rostering.model.OneRosterUser
import world.respect.shared.navigation.AddPersonToClazz
import world.respect.shared.navigation.NavCommand
import world.respect.shared.viewmodel.RespectViewModel

data class ClazzDetailUiState(
    val listOfTeachers: List<OneRosterUser> = emptyList(),
    val listOfStudents: List<OneRosterUser> = emptyList(),
    val listOfPending: List<OneRosterUser> = emptyList(),
    val sortOptions: List<String> = listOf("First", "Last"),
    val selectedSortOption: String? = sortOptions.first(),
    val chipOptions: List<String> = listOf("Active", "All"),
    val selectedChip: String = "All"
)

class ClazzDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    private val fakeRosterDataSource = FakeRosterDataSource()
    private val _uiState = MutableStateFlow(ClazzDetailUiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val users = fakeRosterDataSource.getAllUsers()

            val teachers = users.filter { user ->
                user.enabledUser && user.roles.any { it.role == OneRosterRoleEnum.TEACHER }
            }

            val students = users.filter { user ->
                user.enabledUser && user.roles.any { it.role == OneRosterRoleEnum.STUDENT }
            }
            val pendingInvites = users.filter { user ->
                !user.enabledUser
            }

            _uiState.update {
                it.copy(
                    listOfTeachers = teachers,
                    listOfStudents = students,
                    listOfPending = pendingInvites
                )
            }
        }
    }
    fun onClickAddPersonToClazz() {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                AddPersonToClazz
            )
        )
    }

    fun onClickSortOption(title: String) {
        _uiState.update { it.copy(selectedSortOption = title) }
    }

    fun onSelectChip(chip: String) {
        _uiState.update { it.copy(selectedChip = chip) }
    }

}
