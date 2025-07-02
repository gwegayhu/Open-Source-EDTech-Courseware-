package world.respect.app.viewmodel.lessons.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.app.app.LessonDetail
import world.respect.app.datasource.RespectAppDataSourceProvider
import world.respect.app.datasource.fakeds.FakeOpdsDataSource
import world.respect.app.viewmodel.RespectViewModel
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.navigation.NavCommand

data class LessonDetailUiState(
    val lessonDetail: OpdsPublication? = null,
    val publications: List<OpdsPublication> = emptyList()
)

class LessonDetailViewModel(
    savedStateHandle: SavedStateHandle,
    dataSourceProvider: RespectAppDataSourceProvider
) : RespectViewModel(savedStateHandle) {

    private val dataSource = dataSourceProvider.getDataSource(activeAccount)

    private val _uiState = MutableStateFlow(LessonDetailUiState())

    val uiState = _uiState.asStateFlow()

    private val route: LessonDetail = savedStateHandle.toRoute()

    init {

        viewModelScope.launch {
            _appUiState.update {
                it.copy(title = "")
            }
            dataSource.opdsDataSource.loadOpdsPublication(
                url = route.publicationSelfLink,
                params = DataLoadParams(),
                referrerUrl = route.selfLink,
                expectedPublicationId = route.identifier
            ).collect { result ->
                when (result) {
                    is DataLoadResult -> {
                        _uiState.update {
                            it.copy(
                                lessonDetail = result.data,
                            )
                        }
                    }

                    else -> {
                    }
                }
            }
            dataSource.opdsDataSource.loadOpdsFeed(
                url = route.learningUnitsUrl,
                params = DataLoadParams()
            ).collect { result ->
                when (result) {
                    is DataLoadResult -> {
                        _uiState.update {
                            it.copy(
                                publications = result.data?.publications ?: emptyList(),
                            )
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun onClickLesson() {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                LessonDetail(
                    selfLink = route.selfLink,
                    publicationSelfLink = route.publicationSelfLink,
                    learningUnitsUrl = route.learningUnitsUrl,
                    identifier = route.identifier
                )
            )
        )
    }

}
