package world.respect.shared.domain.account.setpassword

interface SetPasswordUseCase {

    data class SetPasswordRequest(
        val auth: String,
        val userGuid: String,
        val password: String,
    )

    suspend operator fun invoke(
        request: SetPasswordRequest,
    )

}