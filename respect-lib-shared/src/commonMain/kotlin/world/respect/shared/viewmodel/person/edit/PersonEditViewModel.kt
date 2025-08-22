package world.respect.shared.viewmodel.person.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.inject
import org.koin.core.scope.Scope
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataLoadingState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.RespectRealmDataSource
import world.respect.datalayer.ext.dataOrNull
import world.respect.datalayer.ext.isReadyAndSettled
import world.respect.datalayer.realm.model.Person
import world.respect.shared.domain.account.RespectAccountManager
import world.respect.shared.domain.realm.RealmPrimaryKeyGenerator
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.add_person
import world.respect.shared.generated.resources.edit_person
import world.respect.shared.generated.resources.save
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.PersonDetail
import world.respect.shared.navigation.PersonEdit
import world.respect.shared.util.LaunchDebouncer
import world.respect.shared.util.ext.asUiText
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.ActionBarButtonUiState
import kotlin.getValue

data class PersonEditUiState(
    val person: DataLoadState<Person> = DataLoadingState(),
) {
    val fieldsEnabled : Boolean
        get() = person.isReadyAndSettled()
}

class PersonEditViewModel(
    savedStateHandle: SavedStateHandle,
    accountManager: RespectAccountManager,
    private val json: Json,
) : RespectViewModel(savedStateHandle), KoinScopeComponent {

    override val scope: Scope = accountManager.requireSelectedAccountScope()

    private val realmDataSource: RespectRealmDataSource by inject()

    val route: PersonEdit = savedStateHandle.toRoute()

    private val realmPrimaryKeyGenerator: RealmPrimaryKeyGenerator by inject()

    private val guid = route.guid ?: realmPrimaryKeyGenerator.primaryKeyGenerator.nextId(
        Person.TABLE_ID
    ).toString()

    private val _uiState = MutableStateFlow(PersonEditUiState())

    val uiState = _uiState.asStateFlow()

    private val debouncer = LaunchDebouncer(viewModelScope)

    init {
        _appUiState.update { prev ->
            prev.copy(
                title = if(route.guid == null)
                    Res.string.add_person.asUiText()
                else
                    Res.string.edit_person.asUiText(),
                hideBottomNavigation = true,
                actionBarButtonState = ActionBarButtonUiState(
                    visible = true,
                    onClick = ::onClickSave,
                    text = Res.string.save.asUiText(),
                    enabled = false,
                )
            )
        }

        viewModelScope.launch {
            if(route.guid != null) {
                loadEntity(
                    json = json,
                    serializer = Person.serializer(),
                    loadFn = { params ->
                        realmDataSource.personDataSource.findByGuid(params, guid)
                    },
                    uiUpdateFn = { person ->
                        _uiState.update { prev -> prev.copy(person = person) }
                    }
                )
            }else {
                _uiState.update { prev ->
                    prev.copy(
                        person = DataReadyState(
                            Person(
                                guid = guid,
                                givenName = "",
                                familyName = "",
                                roles = emptyList(),
                            )
                        )
                    )
                }
            }

            _appUiState.update { prev ->
                prev.copy(
                    actionBarButtonState = prev.actionBarButtonState.copy(enabled = true)
                )
            }
        }
    }

    fun onEntityChanged(person: Person) {
        val personToCommit = _uiState.updateAndGet { prev ->
            prev.copy(person = DataReadyState(person))
        }.person.dataOrNull() ?: return

        debouncer.launch(DEFAULT_SAVED_STATE_KEY) {
            savedStateHandle[DEFAULT_SAVED_STATE_KEY] = json.encodeToString(personToCommit)
        }
    }

    fun onClickSave() {
        val person = _uiState.value.person.dataOrNull() ?: return
        viewModelScope.launch {
            try {
                realmDataSource.personDataSource.putPerson(person)

                if(route.guid == null) {
                    _navCommandFlow.tryEmit(
                        NavCommand.Navigate(
                            PersonDetail(guid), popUpTo = route, popUpToInclusive = true
                        )
                    )
                }else {
                    _navCommandFlow.tryEmit(NavCommand.PopUp())
                }
            }catch(e: Throwable) {
                //needs to display snack bar here
            }


        }
    }

}