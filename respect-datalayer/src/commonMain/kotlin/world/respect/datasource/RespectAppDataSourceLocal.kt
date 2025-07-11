package world.respect.datasource

import world.respect.datasource.compatibleapps.CompatibleAppsDataSourceLocal
import world.respect.datasource.opds.OpdsDataSourceLocal

/**
 *
 */
interface RespectAppDataSourceLocal: RespectAppDataSource {

    override val compatibleAppsDataSource: CompatibleAppsDataSourceLocal

    override val opdsDataSource: OpdsDataSourceLocal

}
