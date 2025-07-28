package world.respect.shared.viewmodel.clazz.detail

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import world.respect.shared.navigation.AcceptInvite
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.Student
import world.respect.shared.viewmodel.RespectViewModel

data class ClazzDetailUiState(
    val listOfTeachers: List<String> = listOf("Micky", "Mouse", "Bunny"),

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

    fun onClickInviteStudent() {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                Student
            )
        )
    }

}
