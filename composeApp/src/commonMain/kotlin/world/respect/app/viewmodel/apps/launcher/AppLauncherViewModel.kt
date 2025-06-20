package world.respect.app.viewmodel.apps.launcher

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.app
import respect.composeapp.generated.resources.apps
import world.respect.app.app.AppList
import world.respect.app.appstate.FabUiState
import world.respect.app.model.applauncher.AppLauncherModel
import world.respect.app.model.applist.FakeAppDataSource
import world.respect.app.viewmodel.RespectViewModel
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.compatibleapps.model.RespectAppManifest

data class AppLauncherUiState(
    val appList: List<RespectAppManifest> = emptyList(),
    val appLauncherDataList: List<AppLauncherModel> = emptyList(),
)

class AppLauncherViewModel(private val appDataSource: FakeAppDataSource = FakeAppDataSource()) : RespectViewModel() {

    private val _uiState = MutableStateFlow(AppLauncherUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(
                    title = getString(resource = Res.string.apps),
                    showBackButton = false,
                    fabState = FabUiState(
                        visible = true,
                        icon = FabUiState.FabIcon.ADD,
                        text = getString(resource = Res.string.app),
                        onClick = {
                            navController.navigate(AppList)
                        })
                )
            }

        }
        loadAppList()
        loadAppLauncherData()
    }
    private fun loadAppList() {
        viewModelScope.launch {
            appDataSource.getLaunchpadApps(
                loadParams = DataLoadParams()
            ).collect { result ->
                when (result) {
                    is DataLoadResult -> {
                        val appList = result.data ?: emptyList()
                        _uiState.update {
                            it.copy(
                                appList = appList
                            )
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    //mock data for testing purpose
    private fun loadAppLauncherData() {
        val appLauncherData: List<AppLauncherModel> =
            listOf(
                AppLauncherModel(
                    imageText = "Chimple",
                    title = "App 1",
                    category = "Education",
                    ageRange = "3-5"
                ),
                AppLauncherModel(
                    imageText = "Ustad  Mobile",
                    title = "App 2",
                    category = "Games",
                    ageRange = "5-7"
                ),
                AppLauncherModel(
                    imageText = "Khan Academy",
                    title = "App 3",
                    category = "Entertainment",
                    ageRange = "7-10"
                ),
                AppLauncherModel(
                    imageText = "Curious Learning",
                    title = "App 4",
                    category = "Productivity",
                    ageRange = "10-12"
                ),
                AppLauncherModel(
                    imageText = "Dals  Learning",
                    title = "App 5",
                    category = "Learning",
                    ageRange = "12+"
                )
            )

        _uiState.value = _uiState.value.copy(
            appLauncherDataList = appLauncherData
        )
    }
}

