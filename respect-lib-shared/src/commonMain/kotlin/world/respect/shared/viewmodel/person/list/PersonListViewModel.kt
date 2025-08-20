package world.respect.shared.viewmodel.person.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.inject
import org.koin.core.scope.Scope
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataLoadingState
import world.respect.datalayer.RespectRealmDataSource
import world.respect.datalayer.realm.model.composites.PersonListDetails
import world.respect.shared.domain.account.RespectAccountManager
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.people
import world.respect.shared.generated.resources.person
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.PersonDetail
import world.respect.shared.navigation.PersonEdit
import world.respect.shared.util.ext.asUiText
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.FabUiState

data class PersonListUiState(
    val persons: DataLoadState<List<PersonListDetails>> = DataLoadingState(),
)

class PersonListViewModel(
    savedStateHandle: SavedStateHandle,
    accountManager: RespectAccountManager,
) : RespectViewModel(savedStateHandle), KoinScopeComponent {

    override val scope: Scope = accountManager.requireSelectedAccountScope()

    private val realmDataSource: RespectRealmDataSource by inject()

    private val _uiState = MutableStateFlow(PersonListUiState())

    val uiState = _uiState.asStateFlow()

    init {
        _appUiState.update {
            it.copy(
                title = Res.string.people.asUiText(),
                fabState = FabUiState(
                    visible = true,
                    onClick = ::onClickAdd,
                    text = Res.string.person.asUiText(),
                    icon = FabUiState.FabIcon.ADD,
                )
            )
        }

        viewModelScope.launch {
            realmDataSource.personDataSource.findAll(DataLoadParams()).collect {
                _uiState.update { state ->
                    state.copy(persons = it)
                }
            }
        }
    }

    fun onClickItem(person: PersonListDetails) {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(PersonDetail(person.guid))
        )
    }

    fun onClickAdd() {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(PersonEdit(null))
        )
    }

}