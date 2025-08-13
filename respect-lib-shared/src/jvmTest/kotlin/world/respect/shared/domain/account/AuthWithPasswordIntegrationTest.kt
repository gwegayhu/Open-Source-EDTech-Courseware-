package world.respect.shared.domain.account

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.datalayer.db.realm.PersonDataSourceDb
import world.respect.datalayer.db.realm.adapters.toEntities
import world.respect.datalayer.realm.model.Person
import world.respect.libxxhash.XXStringHasher
import world.respect.libxxhash.jvmimpl.XXStringHasherCommonJvm
import world.respect.shared.domain.AuthenticatedUserPrincipalId
import world.respect.shared.domain.account.authwithpassword.GetTokenAndUserProfileWithUsernameAndPasswordDbImpl
import world.respect.shared.domain.account.gettokenanduser.GetTokenAndUserProfileWithUsernameAndPasswordUseCase
import world.respect.shared.domain.account.setpassword.SetPasswordUseCase
import world.respect.shared.domain.account.setpassword.SetPasswordUseDbImpl
import world.respect.shared.domain.account.validateauth.ValidateAuthorizationUseCase
import world.respect.shared.domain.account.validateauth.ValidateAuthorizationUseCaseDbImpl
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AuthWithPasswordIntegrationTest {

    @JvmField
    @Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var realmDb: RespectRealmDatabase

    private lateinit var xxHash: XXStringHasher

    private lateinit var setPasswordUseCase: SetPasswordUseCase

    private lateinit var getTokenUseCase: GetTokenAndUserProfileWithUsernameAndPasswordUseCase

    private lateinit var validateAuthUseCase: ValidateAuthorizationUseCase

    private val defaultTestPerson = Person(
        guid = "42",
        username = "testuser",
        givenName = "John",
        familyName = "Doe"
    )

    @BeforeTest
    fun setup() {
        val dbDir = temporaryFolder.newFolder("dbdir")
        realmDb = Room.databaseBuilder<RespectRealmDatabase>(
            File(dbDir, "realm-test.db").absolutePath
        ).setDriver(BundledSQLiteDriver())
            .build()
        xxHash = XXStringHasherCommonJvm()
        setPasswordUseCase = SetPasswordUseDbImpl(realmDb, xxHash)
        getTokenUseCase = GetTokenAndUserProfileWithUsernameAndPasswordDbImpl(
            realmDb, xxHash, PersonDataSourceDb(realmDb, xxHash)
        )

        validateAuthUseCase = ValidateAuthorizationUseCaseDbImpl(realmDb)
    }

    @Test
    fun givenAuthSet_whenAuthWithPasswordInvoked_thenWillReturnToken() {
        runBlocking {
            val personGuid = "42"
            val password = "password"

            realmDb.getPersonEntityDao().insert(
                defaultTestPerson.toEntities(xxHash).personEntity
            )

            setPasswordUseCase(
                SetPasswordUseCase.SetPasswordRequest(
                    authenticatedUserId = AuthenticatedUserPrincipalId(
                        AuthenticatedUserPrincipalId.DIRECTORY_ADMIN_GUID
                    ),
                    userGuid = personGuid,
                    password = password,
                )
            )

            val authResponse = getTokenUseCase(defaultTestPerson.username!!, password)

            val userIdPrincipal = validateAuthUseCase(
                ValidateAuthorizationUseCase.BearerTokenCredential(
                    token = authResponse.token.accessToken
                )
            )

            assertEquals(authResponse.person.guid, personGuid)
            assertEquals(defaultTestPerson.guid, userIdPrincipal.guid)
        }
    }

    @Test
    fun givenAuthSet_whenAuthPasswordInvokedWithWRongPass_thenWillThrowException() {
        runBlocking {
            var exception: Throwable? = null
            try {
                val personGuid = "42"
                val password = "password"
                realmDb.getPersonEntityDao().insert(
                    defaultTestPerson.toEntities(xxHash).personEntity
                )

                setPasswordUseCase(
                    SetPasswordUseCase.SetPasswordRequest(
                        authenticatedUserId = AuthenticatedUserPrincipalId("foo"),
                        userGuid = personGuid,
                        password = password,
                    )
                )

                getTokenUseCase(defaultTestPerson.username!!, "wrong")
            }catch(e: Throwable) {
                exception = e
            }

            assertNotNull(exception)
        }
    }

}