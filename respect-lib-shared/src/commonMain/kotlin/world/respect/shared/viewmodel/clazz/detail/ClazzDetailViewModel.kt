package world.respect.shared.viewmodel.clazz.detail

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import world.respect.shared.datasource.RespectAppDataSourceProvider
import world.respect.shared.viewmodel.RespectViewModel

data class ClazzDetailUiState(
    val teachers: List<String> = emptyList(),
)

class ClazzDetailViewModel(
    savedStateHandle: SavedStateHandle,
    dataSourceProvider: RespectAppDataSourceProvider,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(ClazzDetailUiState())

    val uiState = _uiState.asStateFlow()
}
