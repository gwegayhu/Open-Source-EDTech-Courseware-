package world.respect.datasource.compatibleapps

import world.respect.datasource.DataLoadState
import world.respect.datasource.compatibleapps.model.RespectAppManifest

interface CompatibleAppsDataSourceLocal: CompatibleAppsDataSource {

    suspend fun upsertCompatibleApps(apps: List<DataLoadState<RespectAppManifest>>)

}