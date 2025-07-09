package world.respect.datasource.db.opds.adapters

import world.respect.datasource.db.opds.entities.OpdsFeedEntity
import world.respect.datasource.db.opds.entities.OpdsGroupEntity

class OpdsFeedEntities(
    val opdsFeed: OpdsFeedEntity,
    val publications: List<OpdsPublicationEntities>,
    val groups: List<OpdsGroupEntity>
)