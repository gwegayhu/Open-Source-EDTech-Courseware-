package world.respect.app.viewmodel.lessondetail

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import world.respect.app.model.lessonlist.LessonListModel
import world.respect.app.viewmodel.RespectViewModel


data class LessonDetailUiState(
    val lessonDetailData: LessonListModel? = null,
    val lessonListData: List<LessonListModel> = emptyList(),

    )

class LessonDetailScreenViewModel : RespectViewModel() {
    private val _uiState = MutableStateFlow(LessonDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loaddata()
    }

    //mock data
    private fun loaddata() {
        val lessonDetailData = LessonListModel("Lesson 1", "01", "English", "02:00","Lesson Outcome/Lesson Objective")
        val lessonListData: List<LessonListModel> = listOf(
            LessonListModel("Lesson 1", "01", "English", "02:00","Lesson Outcome/Lesson Objective"),
            LessonListModel("Lesson 2", "02", "English", "02:00","Lesson Outcome/Lesson Objective"),
            LessonListModel("Lesson 3", "03", "English", "02:00","Lesson Outcome/Lesson Objective"),
            LessonListModel("Lesson 4", "04", "English", "02:00","Lesson Outcome/Lesson Objective"),
            LessonListModel("Lesson 5", "05", "English", "02:00","Lesson Outcome/Lesson Objective"),

            )

        _uiState.value = LessonDetailUiState(
            lessonListData = lessonListData,
                    lessonDetailData = lessonDetailData)
    }
}
