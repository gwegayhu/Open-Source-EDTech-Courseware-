package world.respect.datalayer.db

import world.respect.datalayer.RespectRealmDataSourceLocal
import world.respect.datalayer.db.realm.IndicatorDataSourceDb
import world.respect.datalayer.db.realm.PersonDataSourceDb
import world.respect.datalayer.db.realm.ReportDataSourceDb
import world.respect.datalayer.realm.IndicatorDataSource
import world.respect.datalayer.realm.PersonDataSourceLocal
import world.respect.datalayer.realm.ReportDataSource
import world.respect.libxxhash.XXStringHasher

class RespectRealmDataSourceDb(
    private val realmDb: RespectRealmDatabase,
    private val xxStringHasher: XXStringHasher,
) : RespectRealmDataSourceLocal{

    override val personDataSource: PersonDataSourceLocal by lazy {
        PersonDataSourceDb(realmDb, xxStringHasher)
    }

    override val reportDataSource: ReportDataSource
        get() = ReportDataSourceDb(realmDb)

    override val indicatorDataSource: IndicatorDataSource
        get() = IndicatorDataSourceDb(realmDb)
}