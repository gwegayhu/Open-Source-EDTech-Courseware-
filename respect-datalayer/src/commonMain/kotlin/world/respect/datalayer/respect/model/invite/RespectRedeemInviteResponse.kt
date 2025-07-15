package world.respect.datalayer.respect.model.invite

import io.ktor.http.Url
import kotlinx.serialization.Serializable

/**
 * @property accountSourcedId the User sourcedId for the account
 */
@Serializable
class RespectRedeemInviteState(
    val serverUrl: Url,
    val accountSourcedId: String,
    val lastModified: Long,
    val status: Status,
) {

    enum class Status {

        PENDING, APPROVED, DECLINED

    }

}