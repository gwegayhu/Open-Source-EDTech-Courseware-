package world.respect.shared.viewmodel.clazz.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.inject
import org.koin.core.scope.Scope
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataLoadingState
import world.respect.datalayer.SchoolDataSource
import world.respect.datalayer.ext.dataOrNull
import world.respect.datalayer.oneroster.model.OneRosterClass
import world.respect.datalayer.oneroster.model.OneRosterRoleEnum
import world.respect.datalayer.school.model.Person
import world.respect.datalayer.school.model.PersonRole
import world.respect.shared.domain.account.RespectAccountManager
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.first_name
import world.respect.shared.generated.resources.last_name
import world.respect.shared.generated.resources.all
import world.respect.shared.generated.resources.active
import world.respect.shared.generated.resources.edit
import world.respect.shared.navigation.ClazzEdit
import world.respect.shared.navigation.AddPersonToClazz
import world.respect.shared.navigation.ClazzDetail
import world.respect.shared.navigation.NavCommand
import world.respect.shared.util.FilterChipsOption
import world.respect.shared.util.SortOrderOption
import world.respect.shared.util.ext.asUiText
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.FabUiState
import world.respect.shared.viewmodel.clazz.detail.ClazzDetailViewModel.Companion.ALL
import kotlin.getValue

data class ClazzDetailUiState(
    val listOfTeachers: List<Person> = emptyList(),
    val listOfStudents: List<Person> = emptyList(),
    val listOfPending: List<Person> = emptyList(),
    val chipOptions: List<FilterChipsOption> = emptyList(),
    val selectedChip: String = ALL,
    val sortOptions: List<SortOrderOption> = emptyList(),
    val activeSortOrderOption: SortOrderOption = SortOrderOption(
        Res.string.first_name, 1, true
    ),
    val fieldsEnabled: Boolean = true,
    val clazz: DataLoadState<OneRosterClass> = DataLoadingState(),
    val isPendingExpanded: Boolean = true,
    val isTeachersExpanded: Boolean = true,
    val isStudentsExpanded: Boolean = true
)

class ClazzDetailViewModel(
    savedStateHandle: SavedStateHandle,
    accountManager: RespectAccountManager,
) : RespectViewModel(savedStateHandle), KoinScopeComponent {

    override val scope: Scope = accountManager.requireSelectedAccountScope()

    private val schoolDataSource: SchoolDataSource by inject()

    private val _uiState = MutableStateFlow(ClazzDetailUiState())

    val uiState = _uiState.asStateFlow()

    private val route: ClazzDetail = savedStateHandle.toRoute()

    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(
                    showBackButton = false, fabState = FabUiState(
                        visible = true,
                        icon = FabUiState.FabIcon.EDIT,
                        text = Res.string.edit.asUiText(),
                        onClick = ::onClickEdit
                    )
                )
            }

            val users = schoolDataSource.personDataSource.getAllUsers(route.sourcedId)

            val teachers = users.filter { user ->
                 user.roles.any { it.roleType == PersonRole.RoleType.TEACHER }
            }

            val students = users.filter { user ->
                user.roles.any { it.roleType == PersonRole.RoleType.STUDENT }
            }
            val pendingInvites = users.filter { user ->
                user.roles.any { it.roleType == PersonRole.RoleType.PARENT }
            }


            schoolDataSource.onRoasterDataSource.findClassBySourcedIdAsFlow(
                route.sourcedId
            ).collect { clazz ->
                val sortOptions = listOf(
                    SortOrderOption(
                        fieldMessageId = Res.string.first_name, flag = 1, order = true
                    ), SortOrderOption(
                        fieldMessageId = Res.string.last_name, flag = 2, order = true
                    )
                )
                _uiState.update {
                    it.copy(
                        clazz = clazz,
                        listOfTeachers = teachers,
                        listOfStudents = students,
                        listOfPending = pendingInvites,
                        sortOptions = sortOptions,
                        activeSortOrderOption = sortOptions.first(),
                        chipOptions = listOf(
                            FilterChipsOption(getString(Res.string.all)),
                            FilterChipsOption(getString(Res.string.active))
                        ),
                    )
                }
                _appUiState.update { prev ->
                    prev.copy(
                        title = clazz.dataOrNull()?.title?.asUiText()
                    )
                }
            }
        }
    }

    fun onClickAddPersonToClazz(roleType: OneRosterRoleEnum) {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                AddPersonToClazz.create(roleType)
            )
        )
    }

    fun onSortOrderChanged(sortOption: SortOrderOption) {
        _uiState.update {
            it.copy(activeSortOrderOption = sortOption)
        }
    }

    fun onSelectChip(chip: String) {
        _uiState.update { it.copy(selectedChip = chip) }
    }

    fun onClickAcceptInvite(user: Person) {}

    fun onClickDismissInvite(user: Person) {}


    fun onTogglePendingSection() {
        _uiState.update { it.copy(isPendingExpanded = !it.isPendingExpanded) }
    }

    fun onToggleTeachersSection() {
        _uiState.update { it.copy(isTeachersExpanded = !it.isTeachersExpanded) }
    }

    fun onToggleStudentsSection() {
        _uiState.update { it.copy(isStudentsExpanded = !it.isStudentsExpanded) }
    }

    fun onClickEdit() {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(ClazzEdit(route.sourcedId)
            )
        )
    }

    companion object {
        const val ALL = "All"

    }
}
