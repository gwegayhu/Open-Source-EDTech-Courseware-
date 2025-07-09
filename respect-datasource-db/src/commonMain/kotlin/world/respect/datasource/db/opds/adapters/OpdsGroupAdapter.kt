package world.respect.datasource.db.opds.adapters

import kotlinx.serialization.json.Json
import world.respect.datasource.db.opds.OpdsParentType
import world.respect.datasource.db.opds.entities.OpdsFeedMetadataEntity
import world.respect.datasource.db.opds.entities.OpdsGroupEntity
import world.respect.datasource.db.opds.entities.ReadiumLinkEntity
import world.respect.datasource.opds.model.OpdsGroup
import world.respect.datasource.opds.model.ReadiumLink
import world.respect.lib.primarykeygen.PrimaryKeyGenerator
import world.respect.libxxhash.XXStringHasher

data class OpdsGroupEntities(
    val group: OpdsGroupEntity,
    val metadata: OpdsFeedMetadataEntity,
    val publications: List<OpdsPublicationEntities>,
    val links: List<ReadiumLinkEntity>,
)

fun OpdsGroup.asEntities(
    primaryKeyGenerator: PrimaryKeyGenerator,
    json: Json,
    xxStringHasher: XXStringHasher,
    ofeUid: Long,
    index: Int,
): OpdsGroupEntities {
    val groupUid = primaryKeyGenerator.nextId(OpdsGroupEntity.TABLE_ID)

    fun List<ReadiumLink>?.asEntitiesSub(
        propType: ReadiumLinkEntity.PropertyType
    ): List<ReadiumLinkEntity> {
        return this?.mapIndexed { index, link ->
            link.asEntities(
                pkGenerator = primaryKeyGenerator,
                json = json,
                opdsParentType = OpdsParentType.OPDS_FEED,
                opdsParentUid = ofeUid,
                rlePropType = propType,
                rlePropFk = groupUid,
                rleIndex = index,
            )
        }?.flatten() ?: emptyList()
    }

    return OpdsGroupEntities(
        group = OpdsGroupEntity(
            ogeUid = groupUid,
            ogeOfeUid = ofeUid,
            ogeIndex = index,
        ),
        metadata = metadata.asEntity(
            ofmeOfeUid = ofeUid,
            ofmePropType = OpdsFeedMetadataEntity.PropType.GROUP_METADATA,
            ofmeRelUid = groupUid,
        ),
        publications = publications?.mapIndexed { pubIndex, publication ->
            publication.asEntities(
                dataLoadResult = null,
                primaryKeyGenerator = primaryKeyGenerator,
                json = json,
                xxStringHasher = xxStringHasher,
                opeOfeUid = ofeUid,
                opeOfeIndex = pubIndex
            )
        } ?: emptyList(),
        links = links.asEntitiesSub(ReadiumLinkEntity.PropertyType.OPDS_GROUP_LINKS) +
            navigation.asEntitiesSub(ReadiumLinkEntity.PropertyType.OPDS_GROUP_NAVIGATION)
    )
}


fun OpdsGroupEntities.asModel(
    json: Json
) : OpdsGroup {
    fun List<ReadiumLinkEntity>.asModelsSub(
        propType: ReadiumLinkEntity.PropertyType
    ): List<ReadiumLink> {
        return this.asModels(
            json = json,
            propType = propType,
            propFk = group.ogeUid,
        )
    }

    return OpdsGroup(
        metadata = metadata.asModel(),
        links = links.asModelsSub(ReadiumLinkEntity.PropertyType.OPDS_GROUP_LINKS),
        navigation = links.asModelsSub(ReadiumLinkEntity.PropertyType.OPDS_GROUP_NAVIGATION),
        publications = publications.mapNotNull { publication ->
            publication.asModel(json).data
        }
    )
}

