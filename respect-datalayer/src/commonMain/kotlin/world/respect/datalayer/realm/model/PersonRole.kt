package world.respect.datalayer.realm.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PersonRole(
    val isPrimaryRole: Boolean,
    val roleType: RoleType,
    val beginDate: LocalDate? = null,
    val endDate: LocalDate? = null,
) {

    @Serializable
    enum class RoleType(val value: String, val flag: Int) {
        SITE_ADMINISTRATOR("siteAdministrator", 1),
        STUDENT("student", 2),
        SYSTEM_ADMINISTRATOR("systemAdministrator", 3),
        TEACHER("teacher", 4),
        PARENT("parent", 5),
    }

}