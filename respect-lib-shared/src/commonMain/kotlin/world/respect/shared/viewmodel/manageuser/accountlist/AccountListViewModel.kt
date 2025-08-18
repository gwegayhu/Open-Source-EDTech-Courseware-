package world.respect.shared.viewmodel.manageuser.accountlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.datalayer.RespectRealmDataSource
import world.respect.datalayer.ext.dataOrNull
import world.respect.datalayer.realm.model.Person
import world.respect.libutil.ext.replaceOrAppend
import world.respect.shared.domain.account.RespectAccount
import world.respect.shared.domain.account.RespectAccountAndPerson
import world.respect.shared.domain.account.RespectAccountManager
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.accounts
import world.respect.shared.navigation.GetStartedScreen
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.RespectAppLauncher
import world.respect.shared.util.ext.asUiText
import world.respect.shared.viewmodel.RespectViewModel

data class AccountListUiState(
    val activeAccount: RespectAccountAndPerson? = null,
    val accounts: List<RespectAccountAndPerson> = emptyList(),
)

class AccountListViewModel(
    private val respectAccountManager: RespectAccountManager,
    savedStateHandle: SavedStateHandle
) : RespectViewModel(savedStateHandle){

    private val _uiState = MutableStateFlow(AccountListUiState())

    val uiState = _uiState.asStateFlow()

    init {
        _appUiState.update {
            it.copy(
                title = Res.string.accounts.asUiText(),
                hideBottomNavigation = true,
                userAccountIconVisible = false,
            )
        }

        viewModelScope.launch {
            respectAccountManager.activeAccountAndPersonFlow.collect { accountAndPerson ->
                _uiState.update { prev ->
                    prev.copy(activeAccount = accountAndPerson)
                }
            }

            respectAccountManager.storedAccounts.collectLatest { accountList ->
                _uiState.update { prev ->
                    prev.copy(
                        accounts = accountList.map {
                            RespectAccountAndPerson(
                                account = it,
                                person = Person(
                                    guid = it.userSourcedId,
                                    givenName = "",
                                    familyName = "",
                                    roles = emptyList(),
                                )
                            )
                        }
                    )
                }

                accountList.forEach { account ->
                    launch {
                        val accountScope = respectAccountManager.getOrCreateAccountScope(account)
                        val dataSource: RespectRealmDataSource = accountScope.get()
                        dataSource.personDataSource.findByGuidAsFlow(
                            account.userSourcedId
                        ).collect { person ->
                            _uiState.update { prev ->
                                prev.copy(
                                    accounts = prev.accounts.replaceOrAppend(
                                        RespectAccountAndPerson(
                                            account = account,
                                            person = person.dataOrNull() ?: Person(
                                                guid = account.userSourcedId,
                                                givenName = "",
                                                familyName = "",
                                                roles = emptyList(),
                                            )
                                        )
                                    ) {
                                        it.account.realm.self == account.realm.self &&
                                            it.account.userSourcedId == account.userSourcedId
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun onClickAccount(account: RespectAccount) {
        respectAccountManager.activeAccount = account
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(RespectAppLauncher, clearBackStack = true)
        )
    }

    fun onClickAddAccount() {
        _navCommandFlow.tryEmit(NavCommand.Navigate(GetStartedScreen))
    }


}