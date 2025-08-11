package world.respect.server.domain.account.authwithpassword

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.datalayer.db.realm.PersonDataSourceDb
import world.respect.datalayer.db.realm.adapters.toEntities
import world.respect.datalayer.realm.model.Person
import world.respect.libxxhash.jvmimpl.XXStringHasherCommonJvm
import world.respect.server.domain.account.setpassword.SetPasswordUseDbImpl
import world.respect.server.domain.account.validateauth.ValidateAuthorizationUseCaseDbImpl
import world.respect.shared.domain.account.setpassword.SetPasswordUseCase
import world.respect.shared.domain.account.validateauth.ValidateAuthorizationUseCase
import java.io.File
import kotlin.test.Test

class AuthWithPasswordIntegrationTest {

    @JvmField
    @Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun givenAuthSet_whenAuthWithPasswordInvoked_thenWillReturnToken() {
        val dbDir = temporaryFolder.newFolder("dbdir")

        val realmDb = Room.databaseBuilder<RespectRealmDatabase>(
            File(dbDir, "realm-test.db").absolutePath
        ).setDriver(BundledSQLiteDriver())
        .build()

        val xxHash = XXStringHasherCommonJvm()

        val setAuthUseCase = SetPasswordUseDbImpl(realmDb, xxHash)
        val authWithPasswordUseCase = GetTokenAndUserProfileWithUsernameAndPasswordDbImpl(
            realmDb, xxHash,PersonDataSourceDb(realmDb, xxHash)
        )

        val validateAuthUseCase = ValidateAuthorizationUseCaseDbImpl(realmDb)

        runBlocking {
            val personGuid = "42"
            val password = "password"
            val username = "testuser"
            val person = Person(
                guid = personGuid,
                username = username,
                givenName = "John",
                familyName = "Doe"
            )
            realmDb.getPersonEntityDao().insert(person.toEntities().personEntity)

            setAuthUseCase(
                SetPasswordUseCase.SetPasswordRequest(
                    auth = "foo",
                    userGuid = personGuid,
                    password = password,
                )
            )

            val authResponse = authWithPasswordUseCase(username, password)
            validateAuthUseCase(
                ValidateAuthorizationUseCase.BearerTokenCredential(
                    token = authResponse.token.accessToken
                )
            )
            
        }


    }

}