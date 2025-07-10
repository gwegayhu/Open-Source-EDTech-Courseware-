package world.respect.app.viewmodel.apps.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.apps_detail
import world.respect.app.app.AppsDetail
import world.respect.app.app.LearningUnitDetail
import world.respect.app.app.LearningUnitList
import world.respect.app.datasource.RespectAppDataSourceProvider
import world.respect.app.viewmodel.RespectViewModel
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataLoadState
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.datasource.opds.model.OpdsGroup
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.datasource.opds.model.ReadiumLink
import world.respect.datasource.repository.ext.dataOrNull
import world.respect.libutil.ext.resolve
import world.respect.navigation.NavCommand

data class AppsDetailUiState(
    val appDetail: DataLoadState<RespectAppManifest>? = null,
    val publications: List<OpdsPublication> = emptyList(),
    val navigation: List<ReadiumLink> = emptyList(),
    val group: List<OpdsGroup> = emptyList(),
)

class AppsDetailViewModel(
    savedStateHandle: SavedStateHandle,
    dataSourceProvider: RespectAppDataSourceProvider,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(AppsDetailUiState())

    val uiState = _uiState.asStateFlow()

    private val route: AppsDetail = savedStateHandle.toRoute()

    private val dataSource = dataSourceProvider.getDataSource(activeAccount)

    init {

        viewModelScope.launch {
            _appUiState.update {
                it.copy(title = getString(resource = Res.string.apps_detail))
            }

            dataSource.compatibleAppsDataSource.getAppAsFlow(
                manifestUrl = route.manifestUrl,
                loadParams = DataLoadParams()
            ).collectLatest { result ->
                if (result is DataLoadResult) {
                    _uiState.update {
                        it.copy(
                            appDetail = result
                        )
                    }
                }

                result.dataOrNull()?.learningUnits?.also { learningUnitsUri ->
                    dataSource.opdsDataSource.loadOpdsFeed(
                        url = route.manifestUrl.resolve(
                            learningUnitsUri.toString()
                        ),
                        params = DataLoadParams()
                    ).collect { result ->
                        when (result) {
                            is DataLoadResult -> {
                                _uiState.update {
                                    it.copy(
                                        publications = result.data?.publications ?: emptyList(),
                                        navigation = result.data?.navigation ?: emptyList(),
                                        group = result.data?.groups ?: emptyList()
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    fun onClickLessonList() {
        val appManifest = uiState.value.appDetail?.dataOrNull()
        appManifest?.learningUnits?.toString()?.also { uri ->
            _navCommandFlow.tryEmit(
                NavCommand.Navigate(
                    LearningUnitList.create(
                        opdsFeedUrl = route.manifestUrl.resolve(uri)
                    )
                )
            )
        }
    }

    fun onClickLearningUnit(href: String) {
        println("HREF ADV $href")
        val refererUrl = uiState.value.appDetail?.dataOrNull()?.learningUnits.toString()
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                LearningUnitDetail.create(
                    learningUnitManifestUrl = route.manifestUrl.resolve(href),
                    refererUrl = Url(refererUrl),
                    expectedIdentifier = null
                )
            )
        )
    }


    companion object {
        val BUTTONS_ROW = "buttons_row"
        val LESSON_HEADER = "lesson_header"
        val SCREENSHOT = "screenshot"
        val LEARNING_UNIT_LIST = "learning_unit_list"


    }
}
