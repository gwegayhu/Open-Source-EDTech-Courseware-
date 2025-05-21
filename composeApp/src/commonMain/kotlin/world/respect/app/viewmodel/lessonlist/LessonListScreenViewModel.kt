package world.respect.app.viewmodel.lessonlist

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.lesson_list
import world.respect.app.model.lessonlist.LessonListModel
import world.respect.app.viewmodel.RespectViewModel

data class LessonListUiState(
    val lessonListData: List<LessonListModel> = emptyList(),
)

class LessonListScreenViewModel: RespectViewModel() {
    private val _uiState = MutableStateFlow(LessonListUiState())
    val uiState = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(
                    title = getString(resource = Res.string.lesson_list),)
            }
        }
        loadLessonListData()

    }
    private fun loadLessonListData() {
        val lessonListData: List<LessonListModel> = listOf(
            LessonListModel("Lesson 1", "01", "English", "02:00"),
            LessonListModel("Lesson 2", "02", "English", "02:00"),
            LessonListModel("Lesson 3", "03", "English", "02:00"),
            LessonListModel("Lesson 4", "04", "English", "02:00"),
            LessonListModel("Lesson 5", "05", "English", "02:00"),

            )

        _uiState.value = _uiState.value.copy(
            lessonListData = lessonListData
        )
    }

}