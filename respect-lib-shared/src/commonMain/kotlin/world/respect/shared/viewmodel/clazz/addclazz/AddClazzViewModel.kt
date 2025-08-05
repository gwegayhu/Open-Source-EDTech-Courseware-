package world.respect.shared.viewmodel.clazz.addclazz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.save
import world.respect.shared.generated.resources.classes
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.ActionBarButtonUiState

data class AddClazzUiState(
    var className: String = "",
    var description: String = "",
    var startDate: String = "",
    var endDate: String = "",


    )

class AddClazzViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(AddClazzUiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _appUiState.update { prev ->
                prev.copy(
                    title = getString(Res.string.classes),
                    userAccountIconVisible = false,
                    actionBarButtonState = ActionBarButtonUiState(
                        visible = true,
                        text = getString(Res.string.save)
                    ),
                )
            }
        }
    }
    fun onClassNameChange(newValue: String) {
        _uiState.update {
            it.copy(className = newValue)
        }
    }
    fun onClassDescriptionChange(newValue: String) {
        _uiState.update {
            it.copy(description = newValue)
        }
    }
    fun onStartDateChange(newValue: String) {
        _uiState.update {
            it.copy(startDate = newValue)
        }
    }
    fun onEndDateChange(newValue: String) {
        _uiState.update {
            it.copy(endDate = newValue)
        }
    }
}