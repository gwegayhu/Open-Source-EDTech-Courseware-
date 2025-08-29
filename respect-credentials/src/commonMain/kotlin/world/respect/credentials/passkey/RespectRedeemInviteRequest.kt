package world.respect.credentials.passkey

import kotlinx.datetime.LocalDate
import world.respect.credentials.passkey.model.AuthenticationResponseJSON
import world.respect.datalayer.oneroster.rostering.model.OneRosterGenderEnum
import world.respect.datalayer.respect.model.invite.RespectInviteInfo

@Serializable
class RespectRedeemInviteRequest(
    val inviteInfo: RespectInviteInfo,
    val student: PersonInfo,
    val parentOrGuardian: PersonInfo?,
    val parentOrGuardianRole: GuardianRole?,
    val account: Account,
) {

    @Serializable
    enum class GuardianRole {
        FATHER, MOTHER, OTHER_GUARDIAN
    }

   @Serializable
   data class PersonInfo(
       val name: String ?=null,
       val gender: OneRosterGenderEnum?=null,
       val dateOfBirth: LocalDate?=null,
    )

    @Serializable
    sealed class RedeemInviteCredential

    @Serializable
    data class RedeemInvitePasswordCredential(val password: String)

    @Serializable
    data class RedeemInvitePasskeyCredential(
        val authResponseJson: AuthenticationResponseJSON
    )

    @Serializable
    class Account(
        val username: String,
        val credential: RedeemInviteCredential,
    )

}