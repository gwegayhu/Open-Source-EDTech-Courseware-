package world.respect.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import world.respect.app.model.AppsDetailModel
import world.respect.app.model.LessonItem

data class AppsDetailUiState(
    val appsDetailData: AppsDetailModel? = null
)

class AppsDetailScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppsDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loaddata()
    }

    //mock data
    private fun loaddata() {
        val appsDetailData = AppsDetailModel(
            imageName = "Chimple",
            appName = "Chimple: Kids",
            appDescription = "Chimple kids is an educational app to learn basic reading, writing & math skills",
            lessons = listOf(
                LessonItem("Alphabet a-b"),
                LessonItem("Alphabet a-b"),
                LessonItem("Alphabet a-b"),
                LessonItem("Alphabet a-b")
            )
        )
        _uiState.value = AppsDetailUiState(appsDetailData = appsDetailData)
    }
}
