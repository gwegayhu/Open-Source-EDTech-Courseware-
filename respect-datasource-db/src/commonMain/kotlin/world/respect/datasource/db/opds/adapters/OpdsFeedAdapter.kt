package world.respect.datasource.db.opds.adapters

import kotlinx.serialization.json.Json
import world.respect.datasource.DataLoadResult
import world.respect.datasource.db.opds.entities.OpdsFeedEntity
import world.respect.datasource.db.opds.entities.OpdsFeedMetadataEntity
import world.respect.datasource.db.opds.entities.ReadiumLinkEntity
import world.respect.datasource.db.shared.entities.LangMapEntity
import world.respect.datasource.opds.model.OpdsFeed
import world.respect.lib.primarykeygen.PrimaryKeyGenerator
import world.respect.libxxhash.XXStringHasher

class OpdsFeedEntities(
    val opdsFeed: OpdsFeedEntity,
    val feedMetaData: List<OpdsFeedMetadataEntity>,
    val langMapEntities: List<LangMapEntity>,
    val linkEntities: List<ReadiumLinkEntity>,
    val publications: List<OpdsPublicationEntities>,
    val groups: List<OpdsGroupEntities>
)

fun DataLoadResult<OpdsFeed>.asEntities(
    json: Json,
    primaryKeyGenerator: PrimaryKeyGenerator,
    xxStringHasher: XXStringHasher,
) : OpdsFeedEntities? {
    val url = metaInfo.requireUrl()
    val opdsFeed = this.data ?: return null

    val ofeUid = xxStringHasher.hash(metaInfo.requireUrl().toString())

    val feedMetadata = opdsFeed.metadata.asEntity(
        ofmeOfeUid = ofeUid,
        ofmePropType = OpdsFeedMetadataEntity.PropType.FEED_METADATA,
        ofmeRelUid = ofeUid,
    )

    val groupEntities = opdsFeed.groups?.mapIndexed { index, group ->
        group.asEntities(
            primaryKeyGenerator = primaryKeyGenerator,
            json = json,
            xxStringHasher = xxStringHasher,
            ofeUid = ofeUid,
            index = index,
        )
    } ?: emptyList()

    return OpdsFeedEntities(
        opdsFeed = OpdsFeedEntity(
            ofeUid = ofeUid,
            ofeUrl = url,
            ofeUrlHash = ofeUid,
            ofeLastModifiedHeader = metaInfo.lastModified
        ),
        feedMetaData = buildList {
            add(feedMetadata)
            addAll(groupEntities.map { it.metadata })
        },
        langMapEntities = buildList {
            addAll(groupEntities.flatMap { it.langMapEntities })

        },
        linkEntities = buildList {

        },
        publications = buildList {

        },
        groups = buildList {

        }
    )
}