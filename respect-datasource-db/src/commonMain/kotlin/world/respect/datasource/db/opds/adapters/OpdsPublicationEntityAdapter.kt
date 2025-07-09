package world.respect.datasource.db.opds.adapters

import kotlinx.serialization.json.Json
import world.respect.datasource.DataLoadMetaInfo
import world.respect.datasource.DataLoadResult
import world.respect.datasource.LoadingStatus
import world.respect.datasource.db.opds.OpdsTopParentType
import world.respect.datasource.db.opds.entities.OpdsPublicationEntity
import world.respect.datasource.db.opds.entities.ReadiumLinkEntity
import world.respect.datasource.db.shared.adapters.asEntities
import world.respect.datasource.db.shared.adapters.toModel
import world.respect.datasource.db.shared.entities.LangMapEntity
import world.respect.datasource.db.shared.ext.takeIfNotEmpty
import world.respect.datasource.opds.model.LangMap
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.datasource.opds.model.ReadiumLink
import world.respect.datasource.opds.model.ReadiumMetadata
import world.respect.lib.primarykeygen.PrimaryKeyGenerator
import world.respect.libxxhash.XXStringHasher

data class OpdsPublicationEntities(
    val opdsPublicationEntity: OpdsPublicationEntity,
    val langMapEntities: List<LangMapEntity>,
    val linkEntities: List<ReadiumLinkEntity>,
)

fun OpdsPublication.asEntities(
    dataLoadResult: DataLoadResult<*>?,
    primaryKeyGenerator: PrimaryKeyGenerator,
    json: Json,
    xxStringHasher: XXStringHasher,
    opeOfeUid: Long,
    opeOfeIndex: Int,
): OpdsPublicationEntities {
    val opeUid = primaryKeyGenerator.nextId(OpdsPublicationEntity.TABLE_ID)

    fun List<ReadiumLink>?.toEntitiesSub(
        propType: ReadiumLinkEntity.PropertyType,
    ): List<ReadiumLinkEntity> {
        return this?.mapIndexed { linkIndex, link ->
            link.asEntities(
                pkGenerator = primaryKeyGenerator,
                json = json,
                rleTopTableType = OpdsTopParentType.OPDS_PUBLICATION,
                rleTopParentUid = opeUid,
                rlePropType = propType,
                rleIndex = linkIndex
            )
        }?.flatten() ?: emptyList()
    }

    fun LangMap?.toEntitiesSub(
        propType: LangMapEntity.PropType
    ): List<LangMapEntity> {
        return this?.asEntities(
            lmeTopParentType = LangMapEntity.TopParentType.OPDS_PUBLICATION,
            lmeTopParentUid1 = opeUid,
            lmePropType = propType,
        ) ?: emptyList()
    }

    return OpdsPublicationEntities(
        opdsPublicationEntity = OpdsPublicationEntity(
            opeUid = opeUid,
            opeOfeUid = opeOfeUid,
            opeOfeIndex = opeOfeIndex,
            opeUrl = dataLoadResult?.metaInfo?.url,
            opeUrlHash = dataLoadResult?.metaInfo?.url?.toString()?.let { xxStringHasher.hash(it) } ?: 0,
            opeLastModified = dataLoadResult?.metaInfo?.lastModified ?: 0,
            opeMdIdentifier = metadata.identifier,
            opeMdLanguage = metadata.language,
            opeMdType = metadata.type,
            opeMdDescription = metadata.description,
            opeMdNumberOfPages = metadata.numberOfPages,
            opeMdDuration = metadata.duration,
        ),
        langMapEntities = metadata.title.toEntitiesSub(LangMapEntity.PropType.OPDS_PUB_TITLE) +
            metadata.sortAs.toEntitiesSub(LangMapEntity.PropType.OPDS_PUB_SORT_AS) +
            metadata.subtitle.toEntitiesSub(LangMapEntity.PropType.OPDS_PUB_SUBTITLE),
        linkEntities = links.toEntitiesSub(ReadiumLinkEntity.PropertyType.OPDS_PUB_LINKS) +
                images.toEntitiesSub(ReadiumLinkEntity.PropertyType.OPDS_PUB_IMAGES) +
                readingOrder.toEntitiesSub(ReadiumLinkEntity.PropertyType.OPDS_PUB_READING_ORDER) +
                resources.toEntitiesSub(ReadiumLinkEntity.PropertyType.OPDS_PUB_RESOURCES) +
                toc.toEntitiesSub(ReadiumLinkEntity.PropertyType.OPDS_PUB_TOC)
    )
}

/**
 * Convert a list of database entities to OpdsPublication model class.
 *
 * Caveat: when a model has a nullable list there is no differentiation made between the list value
 * being null and an empty list. The model entity will be restored as null if the list is empty.
 */
fun OpdsPublicationEntities.asModel(
    json: Json,
): DataLoadResult<OpdsPublication> {
    fun List<LangMapEntity>.toModelSub(
        propType: LangMapEntity.PropType
    ): LangMap {
        return this.filter {
            it.lmePropType == propType && it.lmeTopParentUid1 == opdsPublicationEntity.opeUid
        }.toModel()
    }

    fun List<LangMapEntity>.toModelSubOrNull(
        propType: LangMapEntity.PropType
    ): LangMap? {
        return this.filter {
            it.lmePropType == propType && it.lmeTopParentUid1 == opdsPublicationEntity.opeUid
        }.takeIf { it.isNotEmpty() }?.toModel()
    }

    fun List<ReadiumLinkEntity>.asModelsSub(
        propType: ReadiumLinkEntity.PropertyType
    ): List<ReadiumLink> {
        return this.filter {
            it.rlePropType == propType && it.rleTopParentUid == opdsPublicationEntity.opeUid
        }.asModels(
            json = json,
        )
    }

    return DataLoadResult(
        data = OpdsPublication(
            metadata = ReadiumMetadata(
                type = opdsPublicationEntity.opeMdType,
                title = langMapEntities.toModelSub(
                    LangMapEntity.PropType.OPDS_PUB_TITLE
                ),
                sortAs = langMapEntities.toModelSubOrNull(LangMapEntity.PropType.OPDS_PUB_SORT_AS),
                subtitle = langMapEntities.toModelSubOrNull(LangMapEntity.PropType.OPDS_PUB_SUBTITLE),
                identifier = opdsPublicationEntity.opeMdIdentifier,
                language = opdsPublicationEntity.opeMdLanguage,
                description = opdsPublicationEntity.opeMdDescription,
                numberOfPages = opdsPublicationEntity.opeMdNumberOfPages,
                duration = opdsPublicationEntity.opeMdDuration,
            ),
            links = linkEntities.asModelsSub(ReadiumLinkEntity.PropertyType.OPDS_PUB_LINKS),
            images = linkEntities.asModelsSub(
                ReadiumLinkEntity.PropertyType.OPDS_PUB_IMAGES
            ).takeIfNotEmpty(),
            readingOrder = linkEntities.asModelsSub(
                ReadiumLinkEntity.PropertyType.OPDS_PUB_READING_ORDER
            ).takeIfNotEmpty(),
            resources = linkEntities.asModelsSub(
                ReadiumLinkEntity.PropertyType.OPDS_PUB_RESOURCES
            ).takeIfNotEmpty(),
            toc = linkEntities.asModelsSub(
                ReadiumLinkEntity.PropertyType.OPDS_PUB_TOC
            ).takeIfNotEmpty(),
        ),
        metaInfo = DataLoadMetaInfo(
            status = LoadingStatus.LOADED,
            url = opdsPublicationEntity.opeUrl,
            lastModified = opdsPublicationEntity.opeLastModified,
        )
    )
}
