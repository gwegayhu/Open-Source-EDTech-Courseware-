package world.respect.app.viewmodel


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import moe.tlaster.precompose.viewmodel.ViewModel
import org.kodein.di.DI
import world.respect.app.AppLauncherModel
import world.respect.app.appstate.AppUiState

data class AppLauncherUiState(
    val appLauncherDataList: List<AppLauncherModel> = emptyList(),
)

class AppLauncherScreenViewModel(
    private val di: DI
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AppLauncherUiState())
    val uiState = _uiState.asStateFlow()

     val _appUiState = MutableStateFlow(AppUiState())

    init {
        _appUiState.update {
            it.copy(
                title = "AppLauncher"
            )
        }
        loadAppLauncherData()
    }

    //mock data for testing purpose
    private fun loadAppLauncherData() {
        val appLauncherData: List<AppLauncherModel> = listOf(
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
