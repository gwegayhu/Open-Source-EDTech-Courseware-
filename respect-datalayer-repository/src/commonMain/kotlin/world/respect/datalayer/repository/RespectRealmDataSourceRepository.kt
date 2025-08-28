package world.respect.datalayer.repository

import world.respect.datalayer.RespectRealmDataSource
import world.respect.datalayer.RespectRealmDataSourceLocal
import world.respect.datalayer.realm.IndicatorDataSource
import world.respect.datalayer.realm.PersonDataSource
import world.respect.datalayer.realm.ReportDataSource

class RespectRealmDataSourceRepository(
    private val local: RespectRealmDataSourceLocal,
    private val remote: RespectRealmDataSource,
) : RespectRealmDataSource {

    override val personDataSource: PersonDataSource
        get() = TODO("Not yet implemented")

    override val reportDataSource: ReportDataSource
        get() = TODO("Not yet implemented")

    override val indicatorDataSource: IndicatorDataSource
        get() = TODO("Not yet implemented")
}