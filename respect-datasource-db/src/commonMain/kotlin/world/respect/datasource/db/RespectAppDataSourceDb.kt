package world.respect.datasource.db

import kotlinx.serialization.json.Json
import world.respect.datasource.RespectAppDataSourceLocal
import world.respect.datasource.compatibleapps.CompatibleAppsDataSourceLocal
import world.respect.datasource.db.compatibleapps.CompatibleAppDataSourceDb
import world.respect.datasource.opds.OpdsDataSource

class RespectAppDataSourceDb(
    private val respectDatabase: RespectDatabase,
    private val json: Json,
): RespectAppDataSourceLocal {

    override val compatibleAppsDataSource: CompatibleAppsDataSourceLocal by lazy {
        CompatibleAppDataSourceDb(respectDatabase, json)
    }

    override val opdsDataSource: OpdsDataSource
        get() = TODO("Not yet implemented")
}