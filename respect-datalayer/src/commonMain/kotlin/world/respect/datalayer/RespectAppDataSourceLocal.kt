package world.respect.datalayer

import world.respect.datalayer.compatibleapps.CompatibleAppsDataSourceLocal
import world.respect.datalayer.opds.OpdsDataSourceLocal
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSourceLocal

/**
 *
 */
interface RespectAppDataSourceLocal: RespectAppDataSource {

    override val compatibleAppsDataSource: CompatibleAppsDataSourceLocal

    override val opdsDataSource: OpdsDataSourceLocal

    override val realmDirectoryDataSource: RealmDirectoryDataSourceLocal

}
