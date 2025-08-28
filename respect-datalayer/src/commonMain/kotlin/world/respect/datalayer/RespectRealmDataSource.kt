package world.respect.datalayer

import world.respect.datalayer.realm.IndicatorDataSource
import world.respect.datalayer.realm.PersonDataSource
import world.respect.datalayer.realm.ReportDataSource

/**
 * DataSource for data which is specific to a given Realm (eg school - see ARCHITECTURE.md for
 * more info).
 *
 * The DataSource requires a user guid and (for a network client) an authorization token.
 */
interface RespectRealmDataSource {

    val personDataSource: PersonDataSource

    val reportDataSource: ReportDataSource

    val indicatorDataSource: IndicatorDataSource

}