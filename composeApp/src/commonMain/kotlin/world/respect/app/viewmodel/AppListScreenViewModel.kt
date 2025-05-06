package world.respect.app.viewmodel


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import moe.tlaster.precompose.viewmodel.ViewModel
import org.kodein.di.DI
import world.respect.app.AppListModel
import world.respect.app.appstate.AppUiState

data class AppListUiState(
    val appListData: List<AppListModel> = emptyList(),
)

class AppListScreenViewModel(
    private val di: DI
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState = _uiState.asStateFlow()

    val _appUiState = MutableStateFlow(AppUiState())

    init {
        _appUiState.update {
            it.copy(
                title = "AppLauncher"
            )
        }
        loadAppListData()
    }

    //mock data for testing purpose
    private fun loadAppListData() {
        val appListData: List<AppListModel> = listOf(
            AppListModel("1","App Name", "Category", "Age Range"),
            AppListModel("2","App Name", "Category", "Age Range"),
            AppListModel("3","App Name", "Category", "Age Range"),
            AppListModel("4","App Name", "Category", "Age Range"),
            AppListModel("5","App Name", "Category", "Age Range"),

        )

        _uiState.value = _uiState.value.copy(
            appListData = appListData)
    }
}
