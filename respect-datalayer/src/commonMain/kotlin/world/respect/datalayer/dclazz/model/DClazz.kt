package world.respect.datalayer.dclazz.model

import kotlinx.serialization.Serializable

@Serializable
data class DClazz(
    val clazzId: String,
    val name: String,
    val description: String,
)
