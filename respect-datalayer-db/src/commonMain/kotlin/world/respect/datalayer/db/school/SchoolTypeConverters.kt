package world.respect.datalayer.db.school

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import world.respect.datalayer.oneroster.model.OneRosterBaseStatusEnum
import world.respect.datalayer.school.model.PersonRole
import kotlin.time.Instant

class SchoolTypeConverters {

    @TypeConverter
    fun toPersonRoleType(value: Int): PersonRole.RoleType {
        return PersonRole.RoleType.entries.first { it.flag == value }
    }

    @TypeConverter
    fun fromPersonRoleType(value: PersonRole.RoleType): Int {
        return value.flag
    }

    /**
     * Convert a LocalDate to/from a Long. This is always done in millis since epoch at start of day
     * UTC.
     */
    @TypeConverter
    fun toLocalDate(value: Long?) : LocalDate? {
        return value?.let {
            Instant.fromEpochMilliseconds(it)
                .toLocalDateTime(TimeZone.UTC)
                .date
        }
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate?) : Long? {
        return value?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds()
    }


    // --- For OneRosterBaseStatusEnum ---
    @TypeConverter
    fun fromStatus(value: OneRosterBaseStatusEnum): String = value.name

    @TypeConverter
    fun toStatus(value: String): OneRosterBaseStatusEnum =
        OneRosterBaseStatusEnum.valueOf(value)

    // --- For JsonObject ---
    @TypeConverter
    fun fromJsonObject(value: JsonObject?): String? =
        value?.let { Json.encodeToString(it) }

    @TypeConverter
    fun toJsonObject(value: String?): JsonObject? =
        value?.let { Json.decodeFromString<JsonObject>(it) }

}