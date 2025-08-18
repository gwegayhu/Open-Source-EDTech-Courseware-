package world.respect.shared.viewmodel.clazz.addperson

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import world.respect.datalayer.oneroster.rostering.model.OneRosterRoleEnum
import world.respect.shared.generated.resources.Res
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.generated.resources.add_student
import world.respect.shared.generated.resources.add_teacher
import world.respect.shared.navigation.AddPersonToClazz
import world.respect.shared.navigation.ClazzDetail


data class AddPersonToClazzUIState(
    val name: String = ""
)

class AddPersonToClazzViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(AddPersonToClazzUIState())

    val uiState = _uiState.asStateFlow()

    private val route: AddPersonToClazz = savedStateHandle.toRoute()


    init {
        viewModelScope.launch {
            _appUiState.update {
                if (route.roleType == OneRosterRoleEnum.STUDENT) {
                    it.copy(
                        title = getString(Res.string.add_student)
                    )
                } else {
                    it.copy(
                        title = getString(Res.string.add_teacher)
                    )
                }

            }
        }
    }
}