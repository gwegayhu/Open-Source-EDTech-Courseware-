package world.respect.shared.domain.account.authwithpassword

import io.ktor.util.decodeBase64Bytes
import world.respect.datalayer.db.RespectSchoolDatabase
import world.respect.datalayer.db.school.adapters.toEntity
import world.respect.datalayer.school.PersonDataSource
import world.respect.libutil.ext.randomString
import world.respect.libxxhash.XXStringHasher
import world.respect.shared.domain.account.AuthResponse
import world.respect.datalayer.school.model.AuthToken
import world.respect.shared.domain.account.ForbiddenException
import world.respect.shared.domain.account.gettokenanduser.GetTokenAndUserProfileWithUsernameAndPasswordUseCase
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


class GetTokenAndUserProfileWithUsernameAndPasswordDbImpl(
    private val schoolDb: RespectSchoolDatabase,
    private val xxHash: XXStringHasher,
    private val personDataSource: PersonDataSource,
): GetTokenAndUserProfileWithUsernameAndPasswordUseCase {

    override suspend fun invoke(
        username: String,
        password: String
    ): AuthResponse {
        val person = personDataSource.findByUsername(username) ?: throw IllegalArgumentException()
        val personGuidHash = xxHash.hash(person.guid)
        val personPassword = schoolDb.getPersonPasswordEntityDao().findByUid(personGuidHash)
            ?: throw ForbiddenException("Invalid username/password")

        val keySpec = PBEKeySpec(password.toCharArray(), personPassword.authSalt.toByteArray(),
            personPassword.authIterations, personPassword.authKeyLen)
        val keyFactory = SecretKeyFactory.getInstance(personPassword.authAlgorithm)
        val expectedAuthEncoded = personPassword.authEncoded.decodeBase64Bytes()
        val actualAuthEncoded = keyFactory.generateSecret(keySpec).encoded

        if (expectedAuthEncoded.contentEquals(actualAuthEncoded)) {
            val token = AuthToken(
                accessToken = randomString(32),
                timeCreated = System.currentTimeMillis(),
                ttl = TOKEN_DEFAULT_TTL,
            )

            schoolDb.getAuthTokenEntityDao().insert(
                token.toEntity(person.guid, personGuidHash)
            )

            return AuthResponse(
                token = token,
                person = person,
            )
        }else {
            throw ForbiddenException("Invalid username/password")
        }
    }

    companion object {

        const val TOKEN_DEFAULT_TTL = (60 * 60 * 24 * 365)//one year

    }
}