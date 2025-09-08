package world.respect.shared.viewmodel.manageuser.otheroption

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.shared.domain.account.invite.GetInviteInfoUseCase
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.invalid_code
import world.respect.shared.generated.resources.invalid_url
import world.respect.shared.generated.resources.other_options
import world.respect.shared.navigation.LoginScreen
import world.respect.shared.navigation.NavCommand
import world.respect.shared.resources.StringResourceUiText
import world.respect.shared.util.ext.asUiText
import world.respect.shared.viewmodel.RespectViewModel

class OtherOptionsViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(OtherOptionsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _appUiState.update { prev ->
                prev.copy(
                    title = Res.string.other_options.asUiText(),
                    hideBottomNavigation = true,
                    userAccountIconVisible = false,
                )
            }
        }
    }

    fun onLinkChanged(link: String) {
        _uiState.update {
            it.copy(
                link = link,
                errorMessage = null
            )
        }
    }

     fun onClickNext() {
         val link = uiState.value.link
         if (link.isBlank()) {
             _uiState.update {
                 it.copy(errorMessage = StringResourceUiText(Res.string.invalid_code))
             }
             return
         }

         try {
             _navCommandFlow.tryEmit(
                 NavCommand.Navigate(
                     LoginScreen.create(Url(link))
                 )
             )
         }catch(e: Throwable){
             _uiState.update {
                 it.copy(errorMessage = StringResourceUiText(Res.string.invalid_url))
             }
         }
    }
}

data class OtherOptionsUiState(
    val link: String = "",
    val errorMessage: StringResourceUiText? = null
)
