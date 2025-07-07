package world.respect.datasource.db.opds.adapters

import kotlinx.serialization.json.Json
import world.respect.datasource.db.opds.entities.ReadiumLinkEntity
import world.respect.datasource.db.opds.entities.ReadiumLinkEntity.LinkEntityJoinType
import world.respect.datasource.opds.model.ReadiumLink
import world.respect.lib.primarykeygen.PrimaryKeyGenerator

/**
 * Convert a ReadiumLink to a list of database entities. Also generates entities for alternate,
 * subcollection, and children links
 */
fun ReadiumLink.asEntities(
    pkGenerator: PrimaryKeyGenerator,
    json: Json,
    rleTableId: Int,
    rleEntityId: Long,
    rleIndex: Int,
    rleJoinToLinkId: Long = 0,
    rleJoinToLinkType: LinkEntityJoinType? = null,
): List<ReadiumLinkEntity> {
    val rleId = pkGenerator.nextId(ReadiumLinkEntity.TABLE_ID)

    fun List<ReadiumLink>?.subListToEntities(
        joinType: LinkEntityJoinType?
    ): List<ReadiumLinkEntity> {
        return this?.mapIndexed { index, link ->
            link.asEntities(
                pkGenerator = pkGenerator,
                json = json,
                rleTableId = rleTableId,
                rleEntityId = rleEntityId,
                rleIndex = index,
                rleJoinToLinkId = rleId,
                rleJoinToLinkType = joinType,
            )
        }?.flatten() ?: emptyList()
    }

    return listOf(
        ReadiumLinkEntity(
            rleId = rleId,
            rleTableId = rleTableId,
            rleEntityId = rleEntityId,
            rleJoinToLinkId = rleJoinToLinkId,
            rleJoinToLinkType = rleJoinToLinkType,
            rleIndex = rleIndex,
            rleHref = href,
            rleRel = rel,
            rleType = type,
            rleTitle = title,
            rleTemplated = templated,
            rleProperties = properties?.let { json.encodeToString(it) },
            rleHeight = height,
            rleWidth = width,
            rleSize = size,
            rleBitrate = bitrate,
            rleDuration = duration,
            rleLanguage = language,
        )
    ) + alternate.subListToEntities(LinkEntityJoinType.ALTERNATE_OF) +
            children.subListToEntities(LinkEntityJoinType.CHILDREN_OF) +
            subcollections.subListToEntities(LinkEntityJoinType.SUB_COLLECTION_OF)
}

/**
 * Convert a list of database entities to a list of Readium Links
 */
fun List<ReadiumLinkEntity>.asModels(
    json: Json,
    rleJoinToLinkId: Long = 0,
    rleJoinToLinkType: LinkEntityJoinType? = null,
): List<ReadiumLink> {
    return filter {
        it.rleJoinToLinkId == rleJoinToLinkId && it.rleJoinToLinkType == rleJoinToLinkType
    }.sortedBy {
        it.rleIndex
    }.map { thisLink ->
        ReadiumLink(
            href = thisLink.rleHref,
            rel = thisLink.rleRel,
            type = thisLink.rleType,
            title = thisLink.rleTitle,
            templated = thisLink.rleTemplated,
            properties = thisLink.rleProperties?.let { json.decodeFromString(it) },
            height = thisLink.rleHeight,
            width = thisLink.rleWidth,
            size = thisLink.rleSize,
            bitrate = thisLink.rleBitrate,
            duration = thisLink.rleDuration,
            language = thisLink.rleLanguage,
            children = asModels(
                json = json,
                rleJoinToLinkId = thisLink.rleId,
                rleJoinToLinkType = LinkEntityJoinType.CHILDREN_OF,
            ),
            alternate = asModels(
                json = json,
                rleJoinToLinkId = thisLink.rleId,
                rleJoinToLinkType = LinkEntityJoinType.ALTERNATE_OF,
            ),
            subcollections = asModels(
                json = json,
                rleJoinToLinkId = thisLink.rleId,
                rleJoinToLinkType = LinkEntityJoinType.SUB_COLLECTION_OF,
            ),
        )
    }
}


