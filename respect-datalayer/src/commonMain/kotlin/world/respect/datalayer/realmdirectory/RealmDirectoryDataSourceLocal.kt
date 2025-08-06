package world.respect.datalayer.realmdirectory

import world.respect.datalayer.respect.model.RespectRealm

interface RealmDirectoryDataSourceLocal: RealmDirectoryDataSource {

    suspend fun upsertRealm(realm: RespectRealm)

}