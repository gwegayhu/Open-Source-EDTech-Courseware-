package world.respect.datalayer.directory

import world.respect.datalayer.respect.model.RespectRealm

interface RespectDirectoryDataSourceLocal: RespectDirectoryDataSource {

    suspend fun upsertRealm(realm: RespectRealm)

}