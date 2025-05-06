package world.respect.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kodein.di.DI

data class ReportUiState(
    val reportTitle: String = "Report Overview",
    val reportData: List<String> = emptyList(),
)

class ReportScreenViewModel(
    private val di: DI
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
