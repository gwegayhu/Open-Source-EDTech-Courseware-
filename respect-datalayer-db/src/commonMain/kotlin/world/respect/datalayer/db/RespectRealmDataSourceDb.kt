package world.respect.datalayer.db

import world.respect.datalayer.RespectRealmDataSourceLocal
import world.respect.datalayer.db.realm.PersonDataSourceDb
import world.respect.datalayer.oneroster.rostering.OneRosterRosterDataSource
import world.respect.datalayer.realm.PersonDataSourceLocal
import world.respect.libxxhash.XXStringHasher

class RespectRealmDataSourceDb(
    private val realmDb: RespectRealmDatabase,
    private val xxStringHasher: XXStringHasher,
) : RespectRealmDataSourceLocal{

    override val personDataSource: PersonDataSourceLocal by lazy {
        PersonDataSourceDb(realmDb, xxStringHasher)
    }
    override val onRoasterDataSource: OneRosterRosterDataSource
        get() = TODO("Not yet implemented")


}