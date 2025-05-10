package world.respect.app.viewmodel.appsdetail

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import world.respect.app.model.appsdetail.AppsDetailModel
import world.respect.app.model.appsdetail.LessonItem
import world.respect.app.viewmodel.RespectViewModel

data class AppsDetailUiState(
    val appsDetailData: AppsDetailModel? = null
)

class AppsDetailScreenViewModel : RespectViewModel() {
    private val _uiState = MutableStateFlow(AppsDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _appUiState.update {
            it.copy(title = "Apps Detail",)


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
                LessonItem("Alphabet a-b"),
                LessonItem("Alphabet a-b"),
                LessonItem("Alphabet a-b"),
                LessonItem("Alphabet a-b")
            )
        )
        _uiState.value = AppsDetailUiState(appsDetailData = appsDetailData)
    }
}
