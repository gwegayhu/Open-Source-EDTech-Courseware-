package world.respect.datasource.db.opds

import androidx.room.Transactor
import androidx.room.useWriterConnection
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
import world.respect.datasource.db.opds.adapters.asEntities
import world.respect.datasource.db.opds.adapters.asModel
import world.respect.datasource.db.shared.entities.LangMapEntity
import world.respect.datasource.opds.OpdsDataSourceLocal
import world.respect.datasource.opds.model.OpdsFeed
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.lib.primarykeygen.PrimaryKeyGenerator
import world.respect.libxxhash.XXStringHasher

class OpdsDataSourceDb(
    private val respectDatabase: RespectDatabase,
    private val json: Json,
    private val xxStringHasher: XXStringHasher,
    private val primaryKeyGenerator: PrimaryKeyGenerator = PrimaryKeyGenerator(
        RespectDatabase.TABLE_IDS
    ),
): OpdsDataSourceLocal {

    /**
     * Update the database with the given opdsfeed by converting it to entities. Delete any previous
     * entities associated with the given publication.
     */
    override suspend fun updateOpdsFeed(feed: DataLoadResult<OpdsFeed>) {
        val feedUrl = feed.metaInfo.requireUrl()

        val feedEntities = feed.asEntities(
            json = json,
            primaryKeyGenerator = primaryKeyGenerator,
            xxStringHasher = xxStringHasher,
        ) ?: return

        respectDatabase.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                val feedUid = xxStringHasher.hash(feedUrl.toString())
                respectDatabase.getOpdsFeedEntityDao().deleteByFeedUid(feedUid)
                respectDatabase.getOpdsFeedMetadataEntityDao().deleteByFeedUid(feedUid)
                respectDatabase.getLangMapEntityDao().deleteAllByFeedUid(feedUid)
                respectDatabase.getReadiumLinkEntityDao().deleteAllByFeedUid(feedUid)
                respectDatabase.getOpdsPublicationEntityDao().deleteAllByFeedUid(feedUid)
                respectDatabase.getOpdsGroupEntityDao().deleteByFeedUid(feedUid)

                respectDatabase.getOpdsFeedEntityDao().insertList(listOf(feedEntities.opdsFeed))
                respectDatabase.getOpdsFeedMetadataEntityDao().insertList(feedEntities.feedMetaData)
                respectDatabase.getLangMapEntityDao().insertAsync(feedEntities.langMapEntities)
                respectDatabase.getReadiumLinkEntityDao().insertList(feedEntities.linkEntities)
                respectDatabase.getOpdsPublicationEntityDao().insertList(feedEntities.publications)
                respectDatabase.getOpdsGroupEntityDao().insertList(feedEntities.groups)
            }
        }
    }

    override suspend fun updateOpdsPublication(publication: DataLoadResult<OpdsPublication>) {
        val pubData = publication.data ?: return
        val publicationEntities = pubData.asEntities(
            dataLoadResult = publication,
            primaryKeyGenerator = primaryKeyGenerator,
            json = json,
            xxStringHasher = xxStringHasher,
            feedUid = 0,
            groupUid = 0,
            feedIndex = 0,
        )

        respectDatabase.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                respectDatabase.getLangMapEntityDao().deleteByTableAndTopParentType(
                    lmeTopParentType = LangMapEntity.TopParentType.OPDS_PUBLICATION.id,
                    lmeEntityUid1 = publicationEntities.opdsPublicationEntity.opeUid,
                )
            }
        }

    }

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