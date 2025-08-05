package world.respect.shared.viewmodel.clazz.detail

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import world.respect.shared.navigation.AcceptInvite
import world.respect.shared.navigation.AddPersonToClazz
import world.respect.shared.navigation.NavCommand
import world.respect.shared.viewmodel.RespectViewModel

data class ClazzDetailUiState(
    val listOfTeachers: List<String> = listOf("Micky", "Mouse", "Bunny"),
    val sortOptions: List<String> = listOf("First", "Last"),
    val selectedSortOption: String? = null,
    val chipOptions: List<String> = listOf("Active", "All"),
    val selectedChip: String = "All"
)

class ClazzDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(ClazzDetailUiState())

    val uiState = _uiState.asStateFlow()

    fun onClickAcceptInvite() {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                AcceptInvite
            )
        )
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
