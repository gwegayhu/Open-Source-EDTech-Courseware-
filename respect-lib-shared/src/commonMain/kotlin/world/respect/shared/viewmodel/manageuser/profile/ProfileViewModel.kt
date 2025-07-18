package world.respect.shared.viewmodel.manageuser.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.getString
import world.respect.datalayer.oneroster.rostering.model.OneRosterGenderEnum
import world.respect.shared.generated.resources.*
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.ProfileScreen
import world.respect.shared.navigation.SignupScreen
import world.respect.shared.navigation.WaitingForApproval
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.ActionBarButtonUiState

class ProfileViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    private val route: ProfileScreen = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = when (route.type) {
                ProfileType.PARENT, ProfileType.Student -> ProfileUiState(
                    screenTitle = getString(Res.string.your_profile_title),
                    actionBarButtonName = getString(Res.string.next),
                    nameLabel = getString(Res.string.your_name_label),
                    genderLabel = getString(Res.string.your_gender_label),
                    dateOfBirthLabel = getString(Res.string.your_dob_label),
                    note = getString(Res.string.your_note)
                )

                ProfileType.CHILD -> ProfileUiState(
                    screenTitle = getString(Res.string.child_profile_title),
                    actionBarButtonName = getString(Res.string.done),
                    nameLabel = getString(Res.string.child_name_label),
                    genderLabel = getString(Res.string.child_gender_label),
                    dateOfBirthLabel = getString(Res.string.child_dob_label),
                    note = getString(Res.string.child_note)
                )
            }

            _appUiState.update { prev ->
                prev.copy(
                    actionBarButtonState = ActionBarButtonUiState(
                        visible = true,
                        text = uiState.value.actionBarButtonName,
                        onClick = { onClickSave() }
                    ),
                    title = uiState.value.screenTitle,
                    hideBottomNavigation = true,
                    userAccountIconVisible = false
                )
            }
        }

    }


    fun onFullNameChanged(value: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    fullName = value,
                    fullNameError = if (value.isNotBlank()) null else getString(Res.string.full_name_required)
                )
            }
        }

    }

    fun onGenderChanged(value: OneRosterGenderEnum) {
        viewModelScope.launch {
            _uiState.update { prev ->
                prev.copy(
                    gender = value,
                    genderError = if (value != OneRosterGenderEnum.UNSPECIFIED) null else getString(
                        Res.string.gender_required
                    )
                )
            }
        }
    }

    fun onDateOfBirthChanged(value: LocalDate?) {

        viewModelScope.launch {
            _uiState.update { prev ->
                prev.copy(
                    dateOfBirth = value,
                    dateOfBirthError = if (value != null) null else getString(Res.string.dob_required)
                )
            }
        }
    }

    fun onClickSave() {

        viewModelScope.launch {
            val current = _uiState.value
            _uiState.update { prev ->
                prev.copy(
                    fullNameError = if (current.fullName.isBlank()) getString(Res.string.full_name_required) else null,
                    genderError = if (current.gender == OneRosterGenderEnum.UNSPECIFIED) getString(
                        Res.string.gender_required
                    ) else null,
                    dateOfBirthError = if (current.dateOfBirth == null) getString(Res.string.dob_required) else null
                )
            }

            val hasError = listOf(
                current.fullName.isBlank(),
                current.gender == OneRosterGenderEnum.UNSPECIFIED,
                current.dateOfBirth == null
            ).any { it }

            if (hasError){
                return@launch
            }else{
                when (route.type) {
                    ProfileType.PARENT ,ProfileType.Student->{
                        viewModelScope.launch {
                            _navCommandFlow.tryEmit(
                                NavCommand.Navigate(SignupScreen.create(route.type))
                            )
                        }
                    }
                    ProfileType.CHILD ->{
                        viewModelScope.launch {
                            _navCommandFlow.tryEmit(
                                NavCommand.Navigate(WaitingForApproval)
                            )
                        }
                    }
                }
            }
        }
    }
}

data class ProfileUiState(
    val screenTitle: String = "",
    val actionBarButtonName: String = "",
    val nameLabel: String = "",
    val genderLabel: String = "",
    val dateOfBirthLabel: String = "",
    val note: String = "",

    val fullName: String = "",
    val gender: OneRosterGenderEnum = OneRosterGenderEnum.UNSPECIFIED,
    val dateOfBirth: LocalDate? = null,

    val fullNameError: String? = null,
    val genderError: String? = null,
    val dateOfBirthError: String? = null
)
