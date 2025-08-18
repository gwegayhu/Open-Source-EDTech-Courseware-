package world.respect.server.domain.realm.add

import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import world.respect.datalayer.RespectRealmDataSourceLocal
import world.respect.datalayer.db.realmdirectory.ext.virtualHostScopeId
import world.respect.datalayer.realm.model.Person
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSourceLocal
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.shared.domain.AuthenticatedUserPrincipalId
import world.respect.shared.domain.account.setpassword.SetPasswordUseCase
import kotlin.time.ExperimentalTime

/**
 * Used by command line client, potentially web admin UI to add a realm.
 */
@OptIn(ExperimentalTime::class)
class AddRealmUseCase(
    private val directoryDataSource: RealmDirectoryDataSourceLocal
): KoinComponent {

    @Serializable
    data class AddRealmRequest(
        val realm: RespectRealm,
        val dbUrl: String,
        val adminUsername: String,
        val adminPassword: String,
    )

    @Serializable
    data class AddRealmResponse(
        val realm: RespectRealm,
    )

    suspend operator fun invoke(
        request: AddRealmRequest
    ): AddRealmResponse {
        directoryDataSource.addServerManagedRealm(
            request.realm,  request.dbUrl
        )

        val realmScope = getKoin().createScope<RespectRealm>(request.realm.virtualHostScopeId)
        val realmDataSource: RespectRealmDataSourceLocal = realmScope.get()
        val setPasswordUseCase: SetPasswordUseCase = realmScope.get()

        val adminPerson = Person(
            guid = "1",
            username = request.adminUsername,
            givenName = "Admin",
            familyName = "Admin",
        )

        realmDataSource.personDataSource.addPerson(adminPerson)

        setPasswordUseCase(
            SetPasswordUseCase.SetPasswordRequest(
                authenticatedUserId = AuthenticatedUserPrincipalId.directoryAdmin,
                userGuid = adminPerson.guid,
                password = request.adminPassword,
            )
        )

        return AddRealmResponse(request.realm)
    }

    companion object {
        const val DEFAULT_ADMIN_USERNAME = "admin"
    }

}