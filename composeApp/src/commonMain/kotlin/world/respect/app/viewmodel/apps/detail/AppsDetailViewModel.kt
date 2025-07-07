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
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.datasource.opds.model.ReadiumLink
import world.respect.libutil.ext.resolve
import world.respect.navigation.NavCommand

data class AppsDetailUiState(
    val appDetail: DataLoadState<RespectAppManifest>? = null,
    val publications: List<OpdsPublication> = emptyList(),
    val link: List<ReadiumLink> = emptyList(),
)

class AppsDetailViewModel(
    savedStateHandle: SavedStateHandle,
    dataSourceProvider: RespectAppDataSourceProvider,
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(AppsDetailUiState())

    val uiState = _uiState.asStateFlow()

    private val route: AppsDetail = savedStateHandle.toRoute()

    private val dataSource = dataSourceProvider.getDataSource(activeAccount)

    var learningUnits:String?=null

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

                var appManifest = (result as? DataLoadResult)?.data

                learningUnits=appManifest?.learningUnits?.toString()

                /*
                 * Temporarily commented out pending the implementation of this in the real
                 * datasource
                dataSource.opdsDataSource.loadOpdsFeed(
                    url = learningUnits.toString(),
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
                */
            }
        }
    }

    fun onClickLessonList() {
        val appManifest = (uiState.value.appDetail as? DataLoadResult)?.data
        appManifest?.learningUnits?.toString()?.also { url ->
            _navCommandFlow.tryEmit(
                NavCommand.Navigate(
                    LearningUnitList(opdsFeedUrl = url)
                )
            )
        }
    }

    fun onClickLesson(publication: OpdsPublication) {
        val publicationHref = publication.links.find { it.rel?.equals("self") == true }?.href
        val refererUrl = (_uiState.value.appDetail as? DataLoadResult)?.data?.learningUnits?.toString()
        if(refererUrl != null && publicationHref != null) {
            _navCommandFlow.tryEmit(
                NavCommand.Navigate(
                    LearningUnitDetail(
                        learningUnitManifestUrl = Url(refererUrl).resolve(publicationHref).toString(),
                        refererUrl = refererUrl,
                        expectedIdentifier = publication.metadata.identifier?.toString()
                    )
                )
            )
        }
    }
}
