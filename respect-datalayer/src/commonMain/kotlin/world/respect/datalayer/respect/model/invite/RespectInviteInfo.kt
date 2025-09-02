package world.respect.datalayer.respect.model.invite

import kotlinx.serialization.Serializable
import world.respect.datalayer.oneroster.model.OneRosterClassGUIDRef
import world.respect.datalayer.respect.model.SchoolDirectoryEntry

/**
 * @property code the invite code (as provided by the user). An invite code includes a
 *           directory code, then a realm code, and then a code handled by the realm.
 * @property school The realm that the user is invited to
 * @property classGUIDRef The class to which the user is being invited
 * @property className The name of the class to which the user is being invited
 * @property schoolName The name of the school to which the user is being invited
 * @property userInviteType type of invite as per the enum
 */
@Serializable
class RespectInviteInfo(
    val code: String,
    val school: SchoolDirectoryEntry,
    val classGUIDRef: OneRosterClassGUIDRef?,
    val className: String?,
    val schoolName: String?,
    val userInviteType: UserInviteType,
) {

    @Suppress("unused")
    enum class UserInviteType {
        TEACHER, STUDENT_OR_PARENT
    }

}

