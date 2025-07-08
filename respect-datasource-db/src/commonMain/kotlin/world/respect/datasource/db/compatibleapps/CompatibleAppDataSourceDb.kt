package world.respect.datasource.db.compatibleapps

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.json.Json
import world.respect.datasource.DataLoadMetaInfo
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataLoadState
import world.respect.datasource.LoadingStatus
import world.respect.datasource.compatibleapps.CompatibleAppsDataSourceLocal
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.datasource.db.RespectDatabase
import world.respect.datasource.db.compatibleapps.adapters.asEntities
import world.respect.datasource.db.compatibleapps.adapters.asModel
import world.respect.libxxhash.XXStringHasher
import androidx.room.Transactor
import androidx.room.useWriterConnection
import io.ktor.http.Url
import kotlinx.coroutines.flow.combine
import world.respect.datasource.db.compatibleapps.adapters.CompatibleAppEntities
import world.respect.datasource.db.compatibleapps.entities.CompatibleAppEntity

class CompatibleAppDataSourceDb(
    private val respectDb: RespectDatabase,
    private val json: Json,
    private val xxStringHasher: XXStringHasher,
): CompatibleAppsDataSourceLocal {

    override suspend fun upsertCompatibleApps(apps: List<DataLoadResult<RespectAppManifest>>) {
        val entities = apps.mapNotNull { it.asEntities(json, xxStringHasher) }

        respectDb.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                respectDb.getCompatibleAppEntityDao().upsert(
                    entities.map { it.compatibleAppEntity }
                )

                entities.forEach {
                    respectDb.getLangMapEntityDao().deleteByTableAndEntityUid(
                        lmeTableId = CompatibleAppEntity.TABLE_ID,
                        lmeEntityUid1 = it.compatibleAppEntity.caeUid,
                    )
                }

                respectDb.getLangMapEntityDao().insertAsync(
                    entities.flatMap { it.langMapEntities }
                )
            }
        }
    }

    override suspend fun getApp(
        manifestUrl: Url,
        loadParams: DataLoadParams
    ) : DataLoadState<RespectAppManifest> {
        val caeUid = xxStringHasher.hash(manifestUrl.toString())
        val appEntity =  respectDb.getCompatibleAppEntityDao().selectByUid(caeUid)
        val langMapEntities = respectDb.getLangMapEntityDao().selectAllByTableAndEntityId(
            lmeTableId = CompatibleAppEntity.TABLE_ID,
            lmeEntityUid1 = caeUid,
            lmeEntityUid2 = 0
        )

        return appEntity?.asModel(langMapEntities, json) ?: DataLoadResult()
    }

    override fun getAppAsFlow(
        manifestUrl: Url,
        loadParams: DataLoadParams
    ): Flow<DataLoadState<RespectAppManifest>> {
        val caeUid = xxStringHasher.hash(manifestUrl.toString())
        return respectDb.getCompatibleAppEntityDao().selectByUidAsFlow(
            caeUid
        ).combine(
            respectDb.getLangMapEntityDao().selectAllByTableAndEntityIdAsFlow(
                lmeTableId = CompatibleAppEntity.TABLE_ID,
                lmeEntityUid1 = caeUid,
                lmeEntityUid2 = 0
            )
        ) { compatibleAppEntity, langMapEntities ->
            compatibleAppEntity?.asModel(langMapEntities, json) ?: DataLoadResult()
        }
    }

    override fun getAddableApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<DataLoadState<RespectAppManifest>>>> {
        val appEntities = respectDb.getCompatibleAppEntityDao().selectAllAsFlow()
        val langmaps = respectDb.getLangMapEntityDao().selectAllByTableId(CompatibleAppEntity.TABLE_ID)

        return appEntities.combine(langmaps) { appEntities, langmaps ->
            DataLoadResult(
                data = appEntities.map { appEntity ->
                    appEntity.asModel(
                        langMapEntities = langmaps.filter { it.lmeEntityUid1 == appEntity.caeUid },
                        json = json
                    )
                    CompatibleAppEntities(
                        compatibleAppEntity = appEntity,
                        langMapEntities = langmaps.filter { it.lmeEntityUid1 == appEntity.caeUid }
                    ).asModel(json)
                },
                metaInfo = DataLoadMetaInfo(
                    status = LoadingStatus.LOADED,
                    lastModified = appEntities.maxOfOrNull { it.caeLastModified } ?: 0,
                    etag = null,
                    url = null,
                )
            )
        }
    }

    override fun getLaunchpadApps(
        loadParams: DataLoadParams
    ): Flow<DataLoadState<List<DataLoadState<RespectAppManifest>>>> {
        return emptyFlow()
    }

    override suspend fun addAppToLaunchpad(manifestUrl: Url) {
        //Do nothing yet
    }

    override suspend fun removeAppFromLaunchpad(manifestUrl: Url) {
        //Do nothing yet
    }
}