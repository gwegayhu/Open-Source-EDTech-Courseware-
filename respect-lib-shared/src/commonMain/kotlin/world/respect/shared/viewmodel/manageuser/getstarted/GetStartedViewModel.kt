package world.respect.shared.viewmodel.manageuser.getstarted

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import world.respect.datalayer.DataErrorResult
import world.respect.datalayer.DataLoadingState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.NoDataLoadedState
import world.respect.datalayer.RespectAppDataSource
import world.respect.datalayer.respect.model.SchoolDirectoryEntry
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.lets_get_started
import world.respect.shared.generated.resources.school_not_exist_error
import world.respect.shared.navigation.JoinClazzWithCode
import world.respect.shared.navigation.LoginScreen
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.OtherOption
import world.respect.shared.resources.StringResourceUiText
import world.respect.shared.resources.UiText
import world.respect.shared.util.LaunchDebouncer
import world.respect.shared.util.exception.getUiText
import world.respect.shared.util.ext.asUiText
import world.respect.shared.viewmodel.RespectViewModel


class GetStartedViewModel(
    savedStateHandle: SavedStateHandle,
    val respectAppDataSource: RespectAppDataSource
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(GetStartedUiState())
    val uiState = _uiState.asStateFlow()
    private val debouncer = LaunchDebouncer(viewModelScope)

    init {
        _appUiState.update { prev ->
            prev.copy(
                title = Res.string.lets_get_started.asUiText(),
                hideBottomNavigation = true,
                userAccountIconVisible = false,
                showBackButton = false,
            )
        }
    }

    fun onSchoolNameChanged(name: String) {
        _uiState.update { it.copy(schoolName = name) }

        if (name.isBlank()) {
            _uiState.update { it.copy(suggestions = emptyList(), errorMessage = null, showButtons = true) }
            return
        }

        debouncer.launch(RESPECT_REALMS) {
            respectAppDataSource.schoolDirectoryDataSource
                .searchSchools(name)
                .collect { state ->
                    when (state) {
                        is DataLoadingState -> {
                            _uiState.update { it.copy(errorMessage = null, suggestions = emptyList()) }
                        }
                        is DataReadyState -> {
                            _uiState.update {
                                it.copy(
                                    suggestions = state.data,
                                    errorMessage = if (state.data.isEmpty()) {
                                        StringResourceUiText(Res.string.school_not_exist_error)
                                    } else null,
                                    showButtons = state.data.isEmpty()
                                )
                            }
                        }
                        is DataErrorResult -> {
                            _uiState.update {
                                it.copy(
                                    suggestions = emptyList(),
                                    errorMessage = state.error.getUiText(),
                                    showButtons = true
                                )
                            }
                        }
                        is NoDataLoadedState -> {
                            _uiState.update {
                                it.copy(
                                    suggestions = emptyList(),
                                    errorMessage = StringResourceUiText(Res.string.school_not_exist_error),
                                    showButtons = true
                                )
                            }
                        }
                    }
                }
        }
    }

    fun onClickIHaveCode() {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(JoinClazzWithCode)
        )
    }

    fun onSchoolSelected(school: SchoolDirectoryEntry) {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                LoginScreen.create(school.self)
            )
        )
    }

    fun onClickOtherOptions() {
        _navCommandFlow.tryEmit(NavCommand.Navigate(OtherOption))
    }

    companion object {

        const val RESPECT_REALMS = "respectRealms"

    }
}
data class GetStartedUiState(
    val schoolName: String = "",
    val errorText: String? = null,
    val showButtons: Boolean = true,
    val errorMessage: UiText? = null,
    val suggestions: List<SchoolDirectoryEntry> = emptyList()

)
