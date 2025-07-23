package world.respect.shared.viewmodel.clazz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.shared.viewmodel.RespectViewModel
import org.jetbrains.compose.resources.getString
import world.respect.datalayer.dclazz.DClazzDataSource
import world.respect.datalayer.dclazz.model.DClazz
import world.respect.shared.domain.account.RespectAccountManager
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.clazz
import world.respect.shared.util.isActiveUserTeacher
import world.respect.shared.viewmodel.app.appstate.FabUiState

data class ClazzListUiState(
    val clazzes: List<DClazz> = emptyList()
)

class ClazzListViewModel(
    savedStateHandle: SavedStateHandle,
    private val dClazzDataSource: DClazzDataSource,
    private val accountManager: RespectAccountManager,
): RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(ClazzListUiState())

    val uiState: Flow<ClazzListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(
                    title = getString(resource = Res.string.clazz),
                    showBackButton = false,
                    fabState = if(accountManager.isActiveUserTeacher()) {
                        FabUiState(
                            visible = true,
                            text = "Class",
                            icon = FabUiState.FabIcon.ADD,
                            onClick = { }
                        )
                    }else {
                        FabUiState()
                    }
                )
            }
        }

        viewModelScope.launch {
            dClazzDataSource.allClazzesAsList().collect { clazzList ->
                _uiState.update {
                    it.copy(
                        clazzes = clazzList
                    )
                }
            }
        }

    }

}