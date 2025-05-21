package world.respect.app.model.appsdetail

import world.respect.app.model.lessonlist.LessonListModel


data class Images(
    val imageUrl: String
)
data class AppsDetailModel(
    val imageName: String,
    val appName: String,
    val appDescription: String,
    val lessons: List<LessonListModel>,
    val images: List<Images>
)

