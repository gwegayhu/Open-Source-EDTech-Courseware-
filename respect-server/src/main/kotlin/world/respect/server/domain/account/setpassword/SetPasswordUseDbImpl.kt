package world.respect.server.domain.account.setpassword

import io.ktor.util.encodeBase64
import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.datalayer.db.realm.entities.PersonPasswordEntity
import world.respect.libutil.ext.randomString
import world.respect.libxxhash.XXStringHasher
import world.respect.shared.domain.account.setpassword.SetPasswordUseCase
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.text.toCharArray

class SetPasswordUseDbImpl(
    private val realmDb: RespectRealmDatabase,
    private val xxHash: XXStringHasher,
): SetPasswordUseCase {

    override suspend fun invoke(request: SetPasswordUseCase.SetPasswordRequest) {
        val salt = randomString(DEFAULT_SALT_LEN)

        val keySpec = PBEKeySpec(request.password.toCharArray(), salt.toByteArray(),
            DEFAULT_ITERATIONS, DEFAULT_KEY_LEN)

        val keyFactory = SecretKeyFactory.getInstance(KEY_ALGO)

        realmDb.getPersonPasswordEntityDao().upsert(
            PersonPasswordEntity(
                pppGuid = xxHash.hash(request.userGuid),
                authAlgorithm = KEY_ALGO,
                authEncoded = keyFactory.generateSecret(keySpec).encoded.encodeBase64(),
                authSalt = salt,
                authIterations = DEFAULT_ITERATIONS,
                authKeyLen = DEFAULT_KEY_LEN,
            )
        )
    }

    companion object {

        const val DEFAULT_SALT_LEN = 16

        const val DEFAULT_ITERATIONS = 10_000

        const val DEFAULT_KEY_LEN = 512

        const val KEY_ALGO = "PBKDF2WithHmacSHA1"

    }
}