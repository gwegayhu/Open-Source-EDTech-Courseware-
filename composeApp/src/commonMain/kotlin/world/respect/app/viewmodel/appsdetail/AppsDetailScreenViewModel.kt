package world.respect.app.viewmodel.appsdetail

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import world.respect.app.model.appsdetail.AppsDetailModel
import world.respect.app.viewmodel.RespectViewModel
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.apps_detail
import world.respect.app.model.appsdetail.Images
import world.respect.app.model.lessonlist.LessonListModel


data class AppsDetailUiState(
    val appsDetailData: AppsDetailModel? = null
)

class AppsDetailScreenViewModel : RespectViewModel() {
    private val _uiState = MutableStateFlow(AppsDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _appUiState.update {
                it.copy(title = getString(resource = Res.string.apps_detail))
            }
        }
        loaddata()
    }

    //mock data
    private fun loaddata() {
        val appsDetailData = AppsDetailModel(
            imageName = "Chimple",
            appName = "Chimple: Kids",
            appDescription = "Chimple kids is an educational app to learn basic reading, writing & math skills",
            lessons = listOf(
                LessonListModel("Lesson 1", "01", "English", "02:00"),
                LessonListModel("Lesson 2", "02", "English", "02:00"),
                LessonListModel("Lesson 3", "03", "English", "02:00"),
                LessonListModel("Lesson 4", "04", "English", "02:00"),
                LessonListModel("Lesson 5", "05", "English", "02:00"),
            ),
            images = listOf(
                Images(""),
                Images(""),
                Images(""),
            )
        )
        _uiState.value = AppsDetailUiState(appsDetailData = appsDetailData)
    }
}
