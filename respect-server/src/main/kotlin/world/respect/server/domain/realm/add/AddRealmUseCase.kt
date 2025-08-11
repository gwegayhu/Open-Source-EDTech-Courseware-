package world.respect.server.domain.realm.add

import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.datalayer.db.realm.entities.PersonEntity
import world.respect.datalayer.db.realmdirectory.ext.virtualHostScopeId
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSourceLocal
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.shared.util.systemTimeInMillis
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
        val db = realmScope.get<RespectRealmDatabase>()

        println(db)
        db.getPersonEntityDao().insert(
            PersonEntity(
                pGuid = "1",
                pGuidHash = 1L,
                pActive = true,
                pLastModified = systemTimeInMillis(),
                pGivenName = "Admin",
                pFamilyName = "Admin",
            )
        )

        //Add the admin person using an AddPersonUseCase


        return AddRealmResponse(request.realm)
    }

}