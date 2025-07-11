package world.respect.datalayer

import world.respect.datalayer.compatibleapps.CompatibleAppsDataSourceLocal
import world.respect.datalayer.opds.OpdsDataSourceLocal

/**
 *
 */
interface RespectAppDataSourceLocal: RespectAppDataSource {

    override val compatibleAppsDataSource: CompatibleAppsDataSourceLocal

    override val opdsDataSource: OpdsDataSourceLocal

}
