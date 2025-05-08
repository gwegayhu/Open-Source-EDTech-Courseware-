package world.respect.app.viewmodel.enterlink

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EnterLinkUiState(
    val reportData: List<String> = emptyList(),
)

class EnterLnkScreenViewModel(
) : ViewModel() {

    private val _uiState = MutableStateFlow(EnterLinkUiState())
    val uiState = _uiState.asStateFlow()

    //added mock regex just for test purpose
    fun isValidUrl(url: String): Boolean {
        val regex = buildString {
            append("^https?://[\\w.-]+(?:\\.[\\w.-]+)+[/\\w\\-._~:?#[\\]@!$&'()*+,;=.]*$")
        }.toRegex()
        return url.matches(regex)
    }


}
