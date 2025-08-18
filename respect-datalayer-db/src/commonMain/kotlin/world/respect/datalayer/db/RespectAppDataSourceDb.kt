package world.respect.datalayer.db

import kotlinx.serialization.json.Json
import world.respect.datalayer.RespectAppDataSourceLocal
import world.respect.datalayer.db.compatibleapps.CompatibleAppDataSourceDb
import world.respect.datalayer.db.opds.OpdsDataSourceDb
import world.respect.datalayer.db.realmdirectory.RealmDirectoryDataSourceDb
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSourceLocal
import world.respect.lib.primarykeygen.PrimaryKeyGenerator
import world.respect.libxxhash.XXStringHasher

class RespectAppDataSourceDb(
    private val respectAppDatabase: RespectAppDatabase,
    private val json: Json,
    private val xxStringHasher: XXStringHasher,
    private val primaryKeyGenerator: PrimaryKeyGenerator,
): RespectAppDataSourceLocal {

    override val compatibleAppsDataSource by lazy {
        CompatibleAppDataSourceDb(respectAppDatabase, json, xxStringHasher)
    }

    override val opdsDataSource by lazy {
        OpdsDataSourceDb(respectAppDatabase, json, xxStringHasher, primaryKeyGenerator)
    }

    override val realmDirectoryDataSource: RealmDirectoryDataSourceLocal by lazy {
        RealmDirectoryDataSourceDb(
            respectAppDatabase, json, xxStringHasher
        )
    }
}