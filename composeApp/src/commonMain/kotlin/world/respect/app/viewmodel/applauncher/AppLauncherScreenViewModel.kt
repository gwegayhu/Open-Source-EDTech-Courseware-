package world.respect.app.viewmodel.applauncher

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import world.respect.app.model.applauncher.AppLauncherModel
import world.respect.app.app.AppList
import world.respect.app.appstate.AppUiState
import world.respect.app.appstate.FabUiState

data class AppLauncherUiState(
    val appLauncherDataList: List<AppLauncherModel> = emptyList(),
)

class AppLauncherScreenViewModel(navController: NavHostController) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AppLauncherUiState())
    val uiState = _uiState.asStateFlow()

     val _appUiState = MutableStateFlow(AppUiState())
    val appUiState = _appUiState.asStateFlow()

    init {
        _appUiState.value = AppUiState(
            title = "App Launcher",
            fabState = FabUiState(
                visible = true,
                text = "Add",
                icon = FabUiState.FabIcon.ADD,
                onClick = {
                    navController.navigate(AppList)
                }
            )
        )
        loadAppLauncherData()
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
            appLauncherDataList = appLauncherData)
    }
}
