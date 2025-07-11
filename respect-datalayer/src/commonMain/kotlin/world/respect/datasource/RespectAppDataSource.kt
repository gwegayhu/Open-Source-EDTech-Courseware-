package world.respect.datasource

import world.respect.datasource.compatibleapps.CompatibleAppsDataSource
import world.respect.datasource.opds.OpdsDataSource

/**
 * Soon will have XapiDataSource, OneRosterDataSource, etc
 */
interface RespectAppDataSource {

    val compatibleAppsDataSource: CompatibleAppsDataSource

    val opdsDataSource: OpdsDataSource

}
