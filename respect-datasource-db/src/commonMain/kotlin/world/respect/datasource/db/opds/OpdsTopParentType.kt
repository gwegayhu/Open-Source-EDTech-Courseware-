package world.respect.datasource.db.opds

/**
 * The OPDS network models are represented in the database using various 1:many joins e.g. with
 * ReadiumLinkEntity, ReadiumSubjectEntity etc. These joins often represent nested
 * relationships e.g. A ReadiumLinkEntity may represent a ReadiumLink which is a child of
 * another ReadiumLink (via the children, subcollection, or alternate properties).
 *
 * All OPDS entities have a top level parent that is an OpdsFeed or OpdsPublication. All entities
 * keep a direct reference to the top level parent so that:
 * a) Select queries can easily select all entities related to a given OpdsFeed or OpdsPublication
 *    which the adapter can then reassemble into the network model
 * b) Upsert queries (used when data is updated from the network) can easily delete entities related
 *    to old versions of an OpdsFeed or OpdsPublication
 */
enum class OpdsTopParentType {

    OPDS_FEED, OPDS_PUBLICATION

}
