package world.respect.datalayer

import world.respect.datalayer.compatibleapps.CompatibleAppsDataSource
import world.respect.datalayer.opds.OpdsDataSource

/**
 * DataSource that provides app (eg. system) level data that is NOT specific to a given realm
 * e.g. Compatible app listings (from Respect App Manifests), OPDS feeds, Respect Directories (see
 * ARCHITECTURE.md).
 */
interface RespectAppDataSource {

    val compatibleAppsDataSource: CompatibleAppsDataSource

    val opdsDataSource: OpdsDataSource

}
