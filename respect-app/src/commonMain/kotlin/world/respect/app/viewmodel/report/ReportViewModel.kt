package world.respect.app.viewmodel.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import respect.respect_app.generated.resources.Res
import respect.respect_app.generated.resources.report
import world.respect.app.viewmodel.RespectViewModel


class ReportViewModel(
    savedStateHandle: SavedStateHandle
) : RespectViewModel(savedStateHandle) {

    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(
                    title = getString(resource = Res.string.report),
                    showBackButton = false,
                )
            }
        }
    }

}
