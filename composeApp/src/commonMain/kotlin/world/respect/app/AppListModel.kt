package world.respect.app

data class AppListModel
  (
    val id: String,
    val title: String,
    val category: String,
    val ageRange: String,
    var isChecked: Boolean = false
)