package world.respect.shared.domain.account.authwithpassword

import world.respect.shared.domain.account.AuthResponse

interface GetTokenAndUserProfileWithUsernameAndPassword {

    suspend operator fun invoke(
        username: String,
        password: String
    ): AuthResponse

}