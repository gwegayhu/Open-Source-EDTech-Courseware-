package world.respect.datalayer.respect.model.invite

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import world.respect.datalayer.oneroster.rostering.model.OneRosterGenderEnum

@Serializable
class RespectRedeemInviteRequest(
    val inviteInfo: RespectInviteInfo,
    val student: PersonInfo,
    val parentOrGuardian: PersonInfo?,
    val parentOrGuardianRole: GuardianRole?,
    val account: Account,
) {

    enum class GuardianRole {
        FATHER, MOTHER, OTHER_GUARDIAN
    }

    @Serializable
    class PersonInfo(
        val name: String,
        val gender: OneRosterGenderEnum,
        val dateOfBirth: LocalDate,
    )

    @Serializable
    class Account(
        val username: String,
        val credential: String,//can be password or passkey
    )

}
