package world.respect.server.domain.realm.add

import kotlinx.serialization.Serializable
import world.respect.datalayer.directory.RespectDirectoryDataSourceLocal
import world.respect.datalayer.respect.model.RespectRealm

class AddRealmUseCase(
    private val directoryDataSource: RespectDirectoryDataSourceLocal
) {

    @Serializable
    data class AddRealmRequest(
        val realm: RespectRealm,
    )

    suspend operator fun invoke(
        request: AddRealmRequest
    ) {
        directoryDataSource.upsertRealm(request.realm)
    }

}