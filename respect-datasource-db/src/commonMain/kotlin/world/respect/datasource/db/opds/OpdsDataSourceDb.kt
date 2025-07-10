package world.respect.datasource.db.opds

import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataLoadState
import world.respect.datasource.db.RespectDatabase
import world.respect.datasource.db.opds.adapters.OpdsFeedEntities
import world.respect.datasource.db.opds.adapters.asModel
import world.respect.datasource.opds.OpdsDataSource
import world.respect.datasource.opds.model.OpdsFeed
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.libxxhash.XXStringHasher

class OpdsDataSourceDb(
    private val respectDatabase: RespectDatabase,
    private val json: Json,
    private val xxStringHasher: XXStringHasher,
): OpdsDataSource {

    override fun loadOpdsFeed(
        url: Url,
        params: DataLoadParams
    ): Flow<DataLoadState<OpdsFeed>> {
        return respectDatabase.getOpdsFeedEntityDao().findByUrlHashAsFlow(
            xxStringHasher.hash(url.toString())
        ).map { feedEntity ->
            feedEntity?.let {
                OpdsFeedEntities(
                    opdsFeed = feedEntity,
                    feedMetaData = respectDatabase.getOpdsFeedMetadataEntityDao().findByFeedUid(
                        feedEntity.ofeUid),
                    langMapEntities = respectDatabase.getLangMapEntityDao().findAllByFeedUid(feedEntity.ofeUid),
                    linkEntities =respectDatabase.getReadiumLinkEntityDao().findAllByFeedUid(feedEntity.ofeUid),
                    publications = respectDatabase.getOpdsPublicationEntityDao().findByFeedUid(
                        feedEntity.ofeUid),
                    groups = respectDatabase.getOpdsGroupEntityDao().findByFeedUid(feedEntity.ofeUid),
                ).asModel(json)
            } ?: DataLoadResult()
        }
    }

    override fun loadOpdsPublication(
        url: Url,
        params: DataLoadParams,
        referrerUrl: Url?,
        expectedPublicationId: String?
    ): Flow<DataLoadState<OpdsPublication>> {
        return emptyFlow()
    }
}