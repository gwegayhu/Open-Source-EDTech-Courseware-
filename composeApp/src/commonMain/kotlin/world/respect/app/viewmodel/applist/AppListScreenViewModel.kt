package world.respect.app.viewmodel.applist


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import world.respect.app.model.applist.AppListModel
import world.respect.app.viewmodel.RespectViewModel

data class AppListUiState(
    val appListData: List<AppListModel> = emptyList(),
)

class AppListScreenViewModel(
) : RespectViewModel() {

    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _appUiState.update {
            it.copy(
                title="Select app",
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
