package world.respect.datalayer

import world.respect.datalayer.oneroster.rostering.OneRosterRosterDataSource
import world.respect.datalayer.realm.PersonDataSource

/**
 * DataSource for data which is specific to a given Realm (eg school - see ARCHITECTURE.md for
 * more info).
 *
 * The DataSource requires a user guid and (for a network client) an authorization token.
 */
interface RespectRealmDataSource {

    val personDataSource: PersonDataSource

    val onRoasterDataSource: OneRosterRosterDataSource
}