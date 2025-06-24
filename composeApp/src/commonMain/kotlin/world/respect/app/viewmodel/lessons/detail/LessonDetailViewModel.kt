package world.respect.app.viewmodel.lessons.detail

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.app.model.lesson.FakeOpdsDataSource
import world.respect.app.viewmodel.RespectViewModel
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.opds.model.LangMapStringValue
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.datasource.opds.model.ReadiumContributorStringValue
import world.respect.datasource.opds.model.ReadiumLink
import world.respect.datasource.opds.model.ReadiumMetadata
import world.respect.datasource.opds.model.ReadiumSubjectStringValue

data class LessonDetailUiState(
    val lessonDetail: OpdsPublication? = null,
    val publications: List<OpdsPublication> = emptyList()
)

class LessonDetailViewModel(
    private val opdsDataSource: FakeOpdsDataSource = FakeOpdsDataSource()
) : RespectViewModel() {
    private val _uiState = MutableStateFlow(LessonDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(title = "")
            }
            opdsDataSource.loadOpdsPublication(
                url = "",
                params = DataLoadParams(),
                referrerUrl = "",
                expectedPublicationId = ""
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
            opdsDataSource.loadOpdsFeed(
                url = "https://your.api.endpoint/opds/lessons",
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

}
