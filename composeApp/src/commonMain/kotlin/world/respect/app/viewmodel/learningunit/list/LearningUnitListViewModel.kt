package world.respect.app.viewmodel.learningunit.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.lesson_list
import world.respect.app.app.LearningUnitDetail
import world.respect.app.app.LearningUnitList
import world.respect.app.appstate.AppBarSearchUiState
import world.respect.app.datasource.RespectAppDataSourceProvider
import world.respect.app.viewmodel.RespectViewModel
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataReadyState
import world.respect.datasource.opds.model.OpdsFacet
import world.respect.datasource.opds.model.OpdsGroup
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.datasource.opds.model.ReadiumLink
import world.respect.datasource.repository.ext.dataOrNull
import world.respect.libutil.ext.resolve
import world.respect.navigation.NavCommand

data class LearningUnitListUiState(
    val publications: List<OpdsPublication> = emptyList(),
    val navigation: List<ReadiumLink> = emptyList(),
    val group: List<OpdsGroup> = emptyList(),
    val lessonFilter: List<OpdsFacet> = emptyList(),
    val selectedFilterTitle: String? = null,
)

class LearningUnitListViewModel(
    savedStateHandle: SavedStateHandle,
    dataSourceProvider: RespectAppDataSourceProvider
) : RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(LearningUnitListUiState())

    val uiState = _uiState.asStateFlow()

    private val dataSource = dataSourceProvider.getDataSource(activeAccount)

    private val route: LearningUnitList = savedStateHandle.toRoute()

    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(
                    title = getString(resource = Res.string.lesson_list),
                    searchState = AppBarSearchUiState(
                        visible = true
                    )
                )
            }

            dataSource.opdsDataSource.loadOpdsFeed(
                url = route.opdsFeedUrl,
                params = DataLoadParams()
            ).collect { result ->
                when (result) {
                    is DataReadyState -> {
                        _uiState.update {
                            it.copy(
                                navigation = result.data.navigation ?: emptyList(),
                                publications = result.data.publications ?: emptyList(),
                                group = result.data.groups ?: emptyList(),
                                lessonFilter = result.data.facets ?: emptyList(),
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    fun onClickFilter(title: String) {
        _uiState.update { it.copy(selectedFilterTitle = title) }
    }

    fun onClickLearningUnit(href: String) {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                LearningUnitDetail.create(
                    learningUnitManifestUrl = route.opdsFeedUrl.resolve(href),
                    refererUrl = route.opdsFeedUrl,
                    expectedIdentifier = null
                )
            )
        )
    }
    fun onClickPublication(publication: OpdsPublication) {
        val publicationHref = publication.links.find { it.rel?.contains(SELF) == true }?.href.toString()
        val refererUrl = route.opdsFeedUrl.resolve(publicationHref).toString()

        println("LESSON $publicationHref ${route.opdsFeedUrl.resolve(publicationHref)}")
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                LearningUnitDetail.create(
                    learningUnitManifestUrl = route.opdsFeedUrl.resolve(publicationHref),
                    refererUrl = Url(refererUrl),
                    expectedIdentifier = publication.metadata.identifier.toString()
                )
            )
        )
    }
    fun onClickNavigation(navigation: ReadiumLink){
        val navigationHref = navigation.href
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                LearningUnitList.create(
                    opdsFeedUrl = route.opdsFeedUrl.resolve(navigationHref)
                )
            )
        )
    }
    companion object{
        val SELF = "self"

    }
}
