package world.respect.shared.viewmodel.person.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataLoadingState
import world.respect.datalayer.RespectRealmDataSource
import world.respect.datalayer.realm.model.composites.PersonListDetails
import world.respect.shared.domain.account.RespectAccountManager
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.people
import world.respect.shared.util.ext.asUiText
import world.respect.shared.viewmodel.RespectViewModel

data class PersonListUiState(
    val persons: DataLoadState<List<PersonListDetails>> = DataLoadingState(),
)

class PersonListViewModel(
    savedStateHandle: SavedStateHandle,
    private val accountManager: RespectAccountManager,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(PersonListUiState())

    val uiState = _uiState.asStateFlow()

    private val realmDataSource: RespectRealmDataSource = accountManager.requireSelectedAccountScope()
        .get()

    init {
        _appUiState.update {
            it.copy(title = Res.string.people.asUiText())
        }

        viewModelScope.launch {
            realmDataSource.personDataSource.findAll(DataLoadParams()).collect {
                _uiState.update { state ->
                    state.copy(persons = it)
                }
            }
        }
    }

    fun onClickItem() {

    }

}