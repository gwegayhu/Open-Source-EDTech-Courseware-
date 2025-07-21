package world.respect.shared.viewmodel.clazz.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import world.respect.datalayer.clazz.model.FakeClazzList
import world.respect.shared.datasource.RespectAppDataSourceProvider
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.classes
import world.respect.shared.generated.resources.clazz
import world.respect.shared.navigation.ClazzDetail
import world.respect.shared.navigation.NavCommand
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.FabUiState


data class ClazzListUiState(
    val clazzList: List<FakeClazzList> = emptyList()
)

class ClazzListViewModel(
    savedStateHandle: SavedStateHandle,
    dataSourceProvider: RespectAppDataSourceProvider,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(ClazzListUiState())

    val uiState = _uiState.asStateFlow()

    private val dataSource = dataSourceProvider.getDataSource(activeAccount)

    init {
        viewModelScope.launch {

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
                                    ClazzDetail
                                )
                            )
                        }
                    )
                )
            }
            val fakeClazzList = world.respect.datalayer.clazz.model.ClazzDataSource().getClazzList()

            _uiState.update {
                it.copy(clazzList = fakeClazzList)
            }

        }
    }

    fun onClickClazz() {

    }

    companion object {
        val CLAZZ_LIST = "clazz_list"
    }
}

