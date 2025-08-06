package world.respect.datalayer.realm

import kotlinx.serialization.Serializable
import world.respect.datalayer.shared.serialization.InstantISO8601Serializer
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class Person(
    val guid: String,
    val active: Boolean,
    @Serializable(with = InstantISO8601Serializer::class)
    val lastModified: Instant,
    val username: String? = null,
    val givenName: String,
    val familyName: String,
    val middleName: String? = null,
)
