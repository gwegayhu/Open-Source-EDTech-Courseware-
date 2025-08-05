package world.respect.shared.viewmodel.clazz.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import world.respect.datalayer.oneroster.rostering.FakeRosterDataSource
import world.respect.datalayer.oneroster.rostering.model.OneRosterClass
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.classes
import world.respect.shared.generated.resources.clazz
import world.respect.shared.navigation.AddClazz
import world.respect.shared.navigation.ClazzDetail
import world.respect.shared.navigation.NavCommand
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.FabUiState


data class ClazzListUiState(
    val oneRoasterClass: List<OneRosterClass> = emptyList(),
    val sortOptions: List<String> = listOf("First", "Last"),
    val selectedSortOption: String? = sortOptions.first()
)

class ClazzListViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    private val fakeRosterDataSource = FakeRosterDataSource()
    private val _uiState = MutableStateFlow(ClazzListUiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

            val classes = fakeRosterDataSource.getAllClasses()
            _uiState.update {
                it.copy(
                    oneRoasterClass = classes
                )
            }
            _appUiState.update {
                it.copy(
                    title = getString(Res.string.classes),
                    showBackButton = false,
                    fabState = FabUiState(
                        visible = true,
                        icon = FabUiState.FabIcon.ADD,
                        text = getString(resource = Res.string.clazz),
                        onClick = {
                            _navCommandFlow.tryEmit(
                                NavCommand.Navigate(
                                    AddClazz
                                )
                            )
                        }
                    )
                )
            }
        }
    }

    fun onClickSortOption(title: String) {
        _uiState.update { it.copy(selectedSortOption = title) }
    }

    fun onClickClazz() {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                ClazzDetail
            )
        )
    }


}

