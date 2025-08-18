package world.respect.datalayer.db.realm

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import world.respect.datalayer.realm.model.PersonRole
import kotlin.time.Instant

class RealmTypeConverters {

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


}