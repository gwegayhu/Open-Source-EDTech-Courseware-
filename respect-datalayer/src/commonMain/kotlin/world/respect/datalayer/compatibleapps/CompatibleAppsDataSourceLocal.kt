package world.respect.datalayer.compatibleapps

import world.respect.datalayer.DataLoadState
import world.respect.datalayer.compatibleapps.model.RespectAppManifest

interface CompatibleAppsDataSourceLocal: CompatibleAppsDataSource {

    suspend fun upsertCompatibleApps(apps: List<DataLoadState<RespectAppManifest>>)

}