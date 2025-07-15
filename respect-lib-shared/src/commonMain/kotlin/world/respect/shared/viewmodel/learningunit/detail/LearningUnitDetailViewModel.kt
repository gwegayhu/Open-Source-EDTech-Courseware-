package world.respect.shared.viewmodel.learningunit.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.shared.navigation.LearningUnitDetail
import world.respect.shared.datasource.RespectAppDataSourceProvider
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.opds.model.OpdsPublication
import world.respect.shared.viewmodel.app.appstate.getTitle

data class LearningUnitDetailUiState(
    val lessonDetail: OpdsPublication? = null,
)

class LearningUnitDetailViewModel(
    savedStateHandle: SavedStateHandle,
    dataSourceProvider: RespectAppDataSourceProvider
) : RespectViewModel(savedStateHandle) {

    private val dataSource = dataSourceProvider.getDataSource(activeAccount)

    private val _uiState = MutableStateFlow(LearningUnitDetailUiState())

    val uiState = _uiState.asStateFlow()

    private val route: LearningUnitDetail = savedStateHandle.toRoute()

    init {

        viewModelScope.launch {

            dataSource.opdsDataSource.loadOpdsPublication(
                url = route.learningUnitManifestUrl,
                params = DataLoadParams(),
                referrerUrl = route.learningUnitManifestUrl,
                expectedPublicationId = route.expectedIdentifier
            ).collect { result ->
                when (result) {
                    is DataReadyState -> {
                        _uiState.update {
                            it.copy(
                                lessonDetail = result.data,
                            )
                        }
                        _appUiState.update {
                            it.copy(
                                title = result.data.metadata.title.getTitle()
                            )
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }
    companion object{
        const val IMAGE="image/png"
    }
}
