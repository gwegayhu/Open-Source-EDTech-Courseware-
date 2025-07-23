package world.respect.shared.viewmodel.manageuser.accountlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.shared.domain.account.RespectAccount
import world.respect.shared.domain.account.RespectAccountManager
import world.respect.shared.navigation.LoginScreen
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.RespectAppLauncher
import world.respect.shared.viewmodel.RespectViewModel

data class AccountAndName(
    val account: RespectAccount,
    val name: String,
)

data class RespectAccountListUiState(
    val activeAccount: RespectAccount? = null,
    val accounts: List<AccountAndName> = emptyList(),
)

class RespectAccountListViewModel(
    private val respectAccountManager: RespectAccountManager,
    savedStateHandle: SavedStateHandle
) : RespectViewModel(savedStateHandle){

    private val _uiState = MutableStateFlow(RespectAccountListUiState())

    val uiState = _uiState.asStateFlow()

    init {
        _appUiState.update {
            it.copy(
                title = "Accounts",
                hideBottomNavigation = true,
                userAccountIconVisible = false,
            )
        }

        viewModelScope.launch {
            respectAccountManager.storedAccountsFlow.combine(
                respectAccountManager.activeAccountFlow
            ) { storedAccounts, activeAccount ->
                Pair(
                    storedAccounts.filter { it.userSourcedId != activeAccount?.userSourcedId },
                    activeAccount
                )
            }.collect { (storedAccounts, activeAccount) ->
                _uiState.update { prev ->
                    prev.copy(
                        activeAccount = activeAccount,
                        accounts = storedAccounts.map {
                            AccountAndName(
                                account = it,
                                name = it.userSourcedId,
                            )
                        }
                    )
                }
            }
        }
    }

    fun onClickAccount(account: RespectAccount) {
        respectAccountManager.activeAccount = account
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(RespectAppLauncher,clearBackStack = true)
        )
    }

    fun onClickAddAccount() {
        _navCommandFlow.tryEmit(NavCommand.Navigate(LoginScreen))
    }


}