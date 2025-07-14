package world.respect.datalayer.respect.model.invite

import io.ktor.http.Url
import kotlinx.serialization.Serializable
import world.respect.datalayer.oneroster.rostering.model.OneRosterClassGUIDRef

@Serializable
class RespectInviteInfo(
    val code: String,
    val systemUrl: Url,
    val classGUIDRef: OneRosterClassGUIDRef?,
    val className: String?,
    val userInviteType: UserInviteType,
) {

    enum class UserInviteType {
        TEACHER, STUDENT_OR_PARENT
    }

}

