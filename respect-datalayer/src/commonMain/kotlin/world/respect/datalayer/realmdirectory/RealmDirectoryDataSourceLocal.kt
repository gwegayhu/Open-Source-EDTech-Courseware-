package world.respect.datalayer.realmdirectory

import world.respect.datalayer.DataReadyState
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.datalayer.respect.model.RespectRealmDirectory

interface RealmDirectoryDataSourceLocal: RealmDirectoryDataSource {

    suspend fun upsertRealm(
        realm: DataReadyState<RespectRealm>,
        directory: RespectRealmDirectory?
    )

    suspend fun addServerManagedRealm(
        realm: RespectRealm,
        dbUrl: String,
    )

    suspend fun getServerManagedDirectory(): RespectRealmDirectory?

}