package world.respect.datalayer.respect.model.invite

import kotlinx.serialization.Serializable

@Serializable
class RespectPendingInviteState(
    val uid: String,
    val request: RespectRedeemInviteRequest,
    val userSourcedId: String,
    val status: Status,
    val deciderUserSourcedId: String?,
    val lastModified: Long,
) {

    enum class Status {

        PENDING, APPROVED, DECLINED

    }

}

