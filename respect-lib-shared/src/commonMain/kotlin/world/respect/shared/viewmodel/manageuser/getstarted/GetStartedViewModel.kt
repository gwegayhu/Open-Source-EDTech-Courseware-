package world.respect.shared.viewmodel.manageuser.getstarted

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import world.respect.credentials.passkey.GetCredentialUseCase
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.lets_get_started
import world.respect.shared.generated.resources.school_not_exist_error
import world.respect.shared.navigation.JoinClazzWithCode
import world.respect.shared.navigation.LoginScreen
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.OtherOption
import world.respect.shared.navigation.RespectAppLauncher
import world.respect.shared.resources.StringResourceUiText
import world.respect.shared.viewmodel.RespectViewModel


class GetStartedViewModel(
    savedStateHandle: SavedStateHandle,
    getCredentialUseCase: GetCredentialUseCase
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(GetStartedUiState())
    val uiState = _uiState.asStateFlow()

    private val schoolList = listOf(
        School("respect school", "https://testproxy.devserver3.ustadmobile.com/"),
        School("respect 2 school", "https://respect2.com"),
        School("spix school", "https://spix.com"),
        School("ustad school", "https://ustad.com"),
    )
    init {
        viewModelScope.launch {
            _appUiState.update { prev ->
                prev.copy(
                    title = getString(Res.string.lets_get_started),
                    hideBottomNavigation = true,
                    userAccountIconVisible = false,
                    showBackButton = false
                )
            }
        }
        viewModelScope.launch {

            try {
                when (val credentialResult = getCredentialUseCase()) {
                    is GetCredentialUseCase.PasskeyCredentialResult -> {
                        _navCommandFlow.tryEmit(
                            NavCommand.Navigate(RespectAppLauncher)
                        )

                    }

                    is GetCredentialUseCase.PasswordCredentialResult -> {

                    }

                    is GetCredentialUseCase.Error -> {
                        _uiState.update { prev ->
                            prev.copy(
                                errorText = (credentialResult.message),
                            )
                        }
                        println("Error occurred: ${credentialResult.message}")
                    }

                    is GetCredentialUseCase.NoCredentialAvailableResult,
                    is GetCredentialUseCase.UserCanceledResult -> {
                        //do nothing
                    }

                }
            } catch (e: Exception) {
                println("Error occurred: ${e.message}")
            }
        }
    }

    fun onSchoolNameChanged(name: String) {
        val suggestions = if (name.isBlank()) {
            emptyList()
        } else {
            schoolList.filter { it.name.contains(name, ignoreCase = true) }
        }

        _uiState.update {
            it.copy(
                schoolName = name,
                errorMessage = if (suggestions.isEmpty())
                    StringResourceUiText(Res.string.school_not_exist_error) else null,
                suggestions = suggestions,
                showButtons = suggestions.isEmpty()
            )
        }
    }

    fun onClickIHaveCode() {
        viewModelScope.launch {
            _navCommandFlow.tryEmit(
                NavCommand.Navigate(JoinClazzWithCode)
            )
        }
    }

    fun onSchoolSelected(school: School) {
        viewModelScope.launch {
            _navCommandFlow.tryEmit(
                NavCommand.Navigate(LoginScreen)
            )
        }
    }
    fun onClickOtherOptions() {
        viewModelScope.launch {
            _navCommandFlow.tryEmit(NavCommand.Navigate(OtherOption))
        }
    }

}
data class School(
    val name: String,
    val url: String
)
data class GetStartedUiState(
    val schoolName: String = "",
    val errorText: String? = null,
    val showButtons: Boolean = true,
    val errorMessage: StringResourceUiText? = null,
    val suggestions: List<School> = emptyList()

)
