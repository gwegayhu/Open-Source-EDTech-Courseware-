package world.respect.shared.domain.report.model

import kotlinx.serialization.Serializable

@Serializable
data class IndicatorData(
    val name: String = "",
    val description: String = "",
    val sql: String = "",
)
