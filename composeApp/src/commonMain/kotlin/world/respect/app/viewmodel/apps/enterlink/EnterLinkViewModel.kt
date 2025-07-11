package world.respect.app.viewmodel.apps.enterlink

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.enter_link
import respect.composeapp.generated.resources.invalid_url
import world.respect.app.components.UiText
import world.respect.app.app.AppsDetail
import world.respect.app.components.StringResourceUiText
import world.respect.app.datasource.RespectAppDataSourceProvider
import world.respect.app.viewmodel.RespectViewModel
import world.respect.datalayer.DataErrorResult
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataReadyState
import world.respect.navigation.NavCommand

data class EnterLinkUiState(
    val linkUrl: String = "",
    val errorMessage: UiText? = null,
)

class EnterLinkViewModel(
    savedStateHandle: SavedStateHandle,
    dataSourceProvider: RespectAppDataSourceProvider,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(EnterLinkUiState())

    private val dataSource = dataSourceProvider.getDataSource(activeAccount)

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(
                    title = getString(Res.string.enter_link)
                )
            }
        }
    }

    fun onLinkChanged(link: String) {
        _uiState.update {
            it.copy(
                linkUrl = link,
                errorMessage = null,
            )
        }
    }

    fun onClickNext() {
        viewModelScope.launch {
            try {
                val linkUrl = Url(uiState.value.linkUrl)
                val appResult = dataSource.compatibleAppsDataSource.getApp(
                    manifestUrl = linkUrl, loadParams = DataLoadParams()
                )

                if(appResult is DataReadyState) {
                    _navCommandFlow.tryEmit(
                        NavCommand.Navigate(
                            AppsDetail.create(linkUrl)
                        )
                    )
                } else {
                    throw (appResult as? DataErrorResult)?.error ?: IllegalStateException()
                }
            } catch (_: Throwable) {
                _uiState.update {
                    it.copy(
                        errorMessage = StringResourceUiText(Res.string.invalid_url)
                    )
                }
            }
        }
    }

}
