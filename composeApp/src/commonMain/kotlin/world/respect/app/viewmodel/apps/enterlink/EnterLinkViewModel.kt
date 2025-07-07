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
import world.respect.app.app.AppsDetail
import world.respect.app.datasource.RespectAppDataSourceProvider
import world.respect.app.viewmodel.RespectViewModel
import world.respect.datasource.DataErrorResult
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.navigation.NavCommand

data class EnterLinkUiState(
    val linkUrl: String = "",
    val errorMessage: String? = null,
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
            _uiState.update {
                //have to use the valid link here
                it.copy(
                    linkUrl = "",
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

    fun onClickNext()  {
        val linkUrl = uiState.value.linkUrl
        try {
            viewModelScope.launch {
                val appResult = dataSource.compatibleAppsDataSource.getApp(
                    manifestUrl = Url((linkUrl)), loadParams = DataLoadParams()
                )

                if(appResult is DataLoadResult) {
                    _navCommandFlow.tryEmit(
                        NavCommand.Navigate(AppsDetail(manifestUrl = linkUrl))
                    )
                }else {
                    throw (appResult as? DataErrorResult)?.error ?: IllegalStateException()
                }
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    errorMessage = e.message ?: e.toString()
                )
            }
        }
    }

}
