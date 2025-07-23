package world.respect.datalayer.dclazz.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class DAssignment(
    val assignmentId: Long = 0,
    val toClazzId: String = "",
    val learningUnitId: String = "",
    val appManifestUrl: String = "",
    val title: String = "",
    val description: String = "",
    val deadline: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
)
