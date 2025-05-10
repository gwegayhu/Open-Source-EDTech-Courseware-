package world.respect.app.viewmodel.enterlink

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import world.respect.app.viewmodel.RespectViewModel

data class EnterLinkUiState(
    val reportData: List<String> = emptyList(),
)

class EnterLnkScreenViewModel(
) : RespectViewModel() {

    private val _uiState = MutableStateFlow(EnterLinkUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _appUiState.update {
            it.copy(
                title="Enter link",
            )
        }
    }
    //added mock regex just for test purpose
    fun isValidUrl(url: String): Boolean {
        val regex = buildString {
            append("^https?://[\\w.-]+(?:\\.[\\w.-]+)+[/\\w\\-._~:?#[\\]@!$&'()*+,;=.]*$")
        }.toRegex()
        return url.matches(regex)
    }


}
