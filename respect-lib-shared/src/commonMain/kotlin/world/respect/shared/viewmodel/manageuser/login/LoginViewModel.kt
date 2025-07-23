package world.respect.shared.viewmodel.manageuser.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.libutil.ext.resolve
import world.respect.shared.domain.account.RespectAccount
import world.respect.shared.domain.account.RespectAccountManager
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.*
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.RespectAppLauncher
import world.respect.shared.resources.StringResourceUiText
import world.respect.shared.viewmodel.RespectViewModel

data class LoginUiState(
    val userId: String = "",
    val password: String = "",
    val userIdError: StringResourceUiText? = null,
    val passwordError: StringResourceUiText? = null,
)

class LoginViewModel(
    savedStateHandle: SavedStateHandle,
    private val accountManager: RespectAccountManager,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _appUiState.update { prev ->
                prev.copy(
                    title = getString(Res.string.login),
                    hideBottomNavigation = true,
                    userAccountIconVisible = false
                )
            }
        }
    }

    fun onUserIdChanged(userId: String) {
        _uiState.update {
            it.copy(
                userId = userId,
                userIdError = null
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null
            )
        }
    }

    fun onClickLogin() {
        viewModelScope.launch {
            val userID = uiState.value.userId
            val password = uiState.value.password

            _uiState.update {
                it.copy(
                    userIdError = if (userID.isEmpty()) StringResourceUiText(Res.string.userid_required) else null,
                    passwordError = if (password.isEmpty()) StringResourceUiText(Res.string.password_required) else null
                )
            }

            if (uiState.value.userIdError!=null || uiState.value.passwordError!=null) {
                return@launch
            }

            val (username, domain) = userID.split("@")
            val serverUrl = Url("https://$domain/")
            accountManager.activeAccount = RespectAccount(
                userSourcedId = username,
                serverUrls = RespectRealm(
                    xapi = serverUrl.resolve("api/xapi/"),
                    oneRoster = serverUrl.resolve("api/oneroster/"),
                    respectExt = serverUrl.resolve("api/respect/"),
                )
            )

            viewModelScope.launch {
                _navCommandFlow.tryEmit(
                    NavCommand.Navigate(RespectAppLauncher)
                )
            }
        }
    }
}
