package world.respect.app.viewmodel.splash

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.app.app.Acknowledgement
import world.respect.app.viewmodel.RespectViewModel
import world.respect.navigation.NavCommand

data class SplashUiState(
    val isLoading: Boolean = true,
)

class SplashViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            _appUiState.update { prev ->
                prev.copy(
                    hideBottomNavigation = true,
                    hideAppBar = true
                )
            }
            delay(1000)
            _navCommandFlow.tryEmit(NavCommand.Navigate(Acknowledgement))
        }
    }
}
