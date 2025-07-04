package world.respect.app.viewmodel.apps.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
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
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.datasource.opds.model.ReadiumLink
import world.respect.navigation.NavCommand

data class AppsDetailUiState(
    val appDetail: RespectAppManifest? = null,
    val publications: List<OpdsPublication> = emptyList(),
    val link: List<ReadiumLink> = emptyList(),
    val appDetailState: DataLoadState<RespectAppManifest>? = null,
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

            dataSource.compatibleAppsDataSource.getApp(
                manifestUrl = route.manifestUrl,
                loadParams = DataLoadParams()
            ).collectLatest { result ->
                if (result is DataLoadResult) {
                    val appDetail = result.data
                    _uiState.update {
                        it.copy(
                            appDetailState = result,
                            appDetail = appDetail
                        )
                    }
                }
                dataSource.opdsDataSource.loadOpdsFeed(
                    url = uiState.value.appDetail?.learningUnits.toString(),
                    params = DataLoadParams()
                ).collect { result ->
                    when (result) {
                        is DataLoadResult -> {
                            _uiState.update {
                                it.copy(
                                    publications = result.data?.publications ?: emptyList(),
                                    link = result.data?.links ?: emptyList()
                                )
                            }
                        }

                        else -> {}
                    }
                }


            }
        }
    }

    fun onClickLessonList() {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                LearningUnitList(
                    opdsFeedUrl = uiState.value.appDetail?.learningUnits?.toString() ?: ""
                )
            )
        )
    }

    fun onClickLesson(publication: OpdsPublication) {
        val publicationSelfLink = publication.links.find { it.rel?.equals("self") == true }?.href

        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                LearningUnitDetail(
                    learningUnitManifestUrl = publicationSelfLink ?: "",
                    refererUrl = uiState.value.appDetail?.learningUnits.toString(),
                    expectedIdentifier = publication.metadata.identifier?.toString()
                )
            )
        )
    }
}
