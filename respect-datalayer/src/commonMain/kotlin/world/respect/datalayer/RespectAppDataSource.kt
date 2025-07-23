package world.respect.datalayer

import world.respect.datalayer.compatibleapps.CompatibleAppsDataSource
import world.respect.datalayer.opds.OpdsDataSource

/**
 * Soon will have XapiDataSource, OneRosterDataSource, etc
 */
interface RespectAppDataSource {

    val compatibleAppsDataSource: CompatibleAppsDataSource

    val opdsDataSource: OpdsDataSource



}
