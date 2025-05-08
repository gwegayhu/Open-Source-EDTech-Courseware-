package world.respect.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ReportUiState(
    val reportData: List<String> = emptyList(),
)

class ReportScreenViewModel(
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadReport()
    }

    private fun loadReport() {
        val reports: List<String> = emptyList()
        _uiState.value = _uiState.value.copy(
            reportData = reports)
    }
}
