package world.respect.app.viewmodel.lessonlist

import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.lesson_list
import world.respect.app.appstate.AppBarSearchUiState
import world.respect.app.model.lessonlist.LessonListModel
import world.respect.app.viewmodel.RespectViewModel
import world.respect.domain.opds.model.LangMap
import world.respect.domain.opds.model.LangMapStringValue
import world.respect.domain.opds.model.OpdsFacet
import world.respect.domain.opds.model.OpdsFeedMetadata
import world.respect.domain.opds.model.OpdsPublication
import world.respect.domain.opds.model.ReadiumContributorStringValue
import world.respect.domain.opds.model.ReadiumLink
import world.respect.domain.opds.model.ReadiumMetadata
import world.respect.domain.opds.model.ReadiumSubject
import world.respect.domain.opds.model.ReadiumSubjectStringValue

data class LessonListUiState(
    val publications: List<OpdsPublication> = emptyList(),
    val lessonFilter: List<OpdsFacet> = emptyList(),
    val selectedFilterTitle: String? = null
)

class LessonListScreenViewModel : RespectViewModel() {
    private val _uiState = MutableStateFlow(LessonListUiState())
    val uiState = _uiState.asStateFlow()

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
        }
        loadLessonListData()
    }

    private fun loadLessonListData() {
        val publications: List<OpdsPublication> = listOf(
            OpdsPublication(
                metadata = ReadiumMetadata(
                    title = LangMapStringValue("Lesson 001"),
                    author = listOf(
                        ReadiumContributorStringValue("Mullah Nasruddin")
                    ),
                    language = listOf("en"),
                    modified = "2015-09-29T17:00:00Z",
                    subject = listOf(
                        ReadiumSubjectStringValue("English"),
                    ),
                    duration = 2.0

                ),
                links = listOf(
                    ReadiumLink(
                        href = "",
                        type = "application/opds-publication+json",
                        rel = listOf("self")
                    ),
                    ReadiumLink(
                        href = "",
                        type = "text/html",
                        rel = listOf("http://opds-spec.org/acquisition/open-access")
                    )
                ),
                images = listOf(
                    ReadiumLink(
                        href = "",
                        type = "image/jpeg",
                        height = 700,
                        width = 400
                    )
                )
            ),
            OpdsPublication(
                metadata = ReadiumMetadata(
                    title = LangMapStringValue("Lesson 005"),
                    author = listOf(
                        ReadiumContributorStringValue("Mullah Nasruddin")
                    ),
                    language = listOf("en"),
                    modified = "2015-09-29T17:00:00Z",
                    subject = listOf(
                        ReadiumSubjectStringValue("Mathematics"),
                    ),
                    duration = 1.0


                ),
                links = listOf(
                    ReadiumLink(
                        href = "",
                        type = "application/opds-publication+json",
                        rel = listOf("self")
                    ),
                    ReadiumLink(
                        href = "",
                        type = "text/html",
                        rel = listOf("http://opds-spec.org/acquisition/open-access")
                    )
                ),
                images = listOf(
                    ReadiumLink(
                        href = "",
                        type = "image/jpeg",
                        height = 700,
                        width = 400
                    )
                )
            )
        )

        val lessonFilter: List<OpdsFacet> = listOf(
            OpdsFacet(
                metadata = OpdsFeedMetadata(
                    title = "Language",
                    identifier = null,
                    type = null,
                    subtitle = null,
                    modified = null,
                    description = null,
                    itemsPerPage = null,
                    currentPage = null,
                    numberOfItems = null,
                ),
                links = listOf(
                    ReadiumLink(
                        href = "/fr",
                        type = "application/opds+json",
                        title = "French"
                    ),
                    ReadiumLink(
                        href = "/en",
                        type = "application/opds+json",
                        title = "English"
                    )
                )
            )
        )

        _uiState.value = _uiState.value.copy(
            publications = publications,
            lessonFilter = lessonFilter,
            selectedFilterTitle = null
        )
    }

    fun onFilterSelected(title: String) {
        _uiState.update { it.copy(selectedFilterTitle = title) }
    }
}