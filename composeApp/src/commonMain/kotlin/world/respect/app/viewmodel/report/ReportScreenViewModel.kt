package world.respect.app.viewmodel.report

import kotlinx.coroutines.flow.update
import world.respect.app.viewmodel.RespectViewModel


class ReportScreenViewModel(
) : RespectViewModel() {

    init {
        _appUiState.update {
            it.copy(
                title="Report",
                showBackButton = false,
            )
        }
    }

}
