package world.respect.app.viewmodel.lessonlist

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
import world.respect.domain.opds.model.OpdsFacet
import world.respect.domain.opds.model.OpdsFeedMetadata
import world.respect.domain.opds.model.ReadiumLink

data class LessonListUiState(
    val lessonListData: List<LessonListModel> = emptyList(),
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
        val lessonListData: List<LessonListModel> = listOf(
            LessonListModel(
                "Lesson 1",
                "01",
                "English",
                "02:00",
                "Lesson Outcome/Lesson Objective"
            ),
            LessonListModel(
                "Lesson 2",
                "02",
                "English",
                "02:00",
                "Lesson Outcome/Lesson Objective"
            ),
            LessonListModel(
                "Lesson 3",
                "03",
                "English",
                "02:00",
                "Lesson Outcome/Lesson Objective"
            ),
            LessonListModel(
                "Lesson 4",
                "04",
                "English",
                "02:00",
                "Lesson Outcome/Lesson Objective"
            ),
            LessonListModel(
                "Lesson 5",
                "05",
                "English",
                "02:00",
                "Lesson Outcome/Lesson Objective"
            ),
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
            lessonListData = lessonListData,
            lessonFilter = lessonFilter,
            selectedFilterTitle = null
        )
    }

    fun onFilterSelected(title: String) {
        _uiState.update { it.copy(selectedFilterTitle = title) }
    }
}