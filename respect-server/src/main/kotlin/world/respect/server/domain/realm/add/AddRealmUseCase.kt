package world.respect.server.domain.realm.add

import kotlinx.serialization.Serializable
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSourceLocal
import world.respect.datalayer.respect.model.RespectRealm

/**
 * Used by command line client, potentially web admin UI,
 */
class AddRealmUseCase(
    private val directoryDataSource: RealmDirectoryDataSourceLocal
) {

    @Serializable
    data class AddRealmRequest(
        val realm: RespectRealm,
    )

    @Serializable
    data class AddRealmResponse(
        val realm: RespectRealm,
    )

    suspend operator fun invoke(
        request: AddRealmRequest
    ): AddRealmResponse {
        directoryDataSource.upsertRealm(request.realm)
        return AddRealmResponse(request.realm)
    }

}