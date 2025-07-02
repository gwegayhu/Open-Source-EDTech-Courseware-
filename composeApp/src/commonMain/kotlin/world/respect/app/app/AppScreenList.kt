package world.respect.app.app

import kotlinx.serialization.Serializable
import world.respect.datasource.opds.model.OpdsPublication

sealed interface AppDestination

@Serializable
object AppLauncher : AppDestination

@Serializable
object Assignment : AppDestination

@Serializable
object Clazz : AppDestination

@Serializable
object Report : AppDestination

@Serializable
object AppList : AppDestination

@Serializable
object EnterLink : AppDestination

@Serializable
data class AppsDetail(
    val manifestUrl: String,
    val url: String
) : AppDestination

@Serializable
data class LessonList(
    val url: String
) : AppDestination

@Serializable
data class LessonDetail(
    val selfLink: String,
    val publicationSelfLink: String,
    val url: String,
    val identifier: String

) : AppDestination

