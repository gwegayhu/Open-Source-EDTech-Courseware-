package world.respect.datasource

import world.respect.datasource.compatibleapps.CompatibleAppsDataSource

/**
 * Soon will have XapiDataSource, OneRosterDataSource, etc
 */
interface RespectAppDataSource {

    val compatibleAppsDataSource: CompatibleAppsDataSource

}
