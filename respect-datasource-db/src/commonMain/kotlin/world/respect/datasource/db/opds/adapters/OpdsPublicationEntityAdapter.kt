package world.respect.datasource.db.opds.adapters

import world.respect.datasource.DataLoadResult
import world.respect.datasource.db.opds.entities.OpdsPublicationEntity
import world.respect.datasource.db.opds.entities.ReadiumLinkEntity
import world.respect.datasource.db.shared.entities.LangMapEntity
import world.respect.datasource.opds.model.OpdsPublication
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
    xxStringHasher: XXStringHasher,
    opeOfeUid: Long,
    opeOfeIndex: Int,

): OpdsPublicationEntities {
    val opeUid = primaryKeyGenerator.nextId(OpdsPublicationEntity.TABLE_ID)
    TODO()

//    OpdsPublicationEntity(
//        opeUid = opeUid,
//        opeOfeUid = opeOfeUid,
//        opeOfeIndex = opeOfeIndex,
//        opeUrl = dataLoadResult?.metaInfo?.url,
//        opeUrlHash = dataLoadResult?.metaInfo?.url?.toString()?.let {
//            xxStringHasher.hash(it)
//        } ?: 0,
//        opeIdentifier = metadata.identifier,
//        opeLanguage = metadata.language,
//        opeSubjectSortAs = metadata.subject?.,
//        opeSubjectCode = metadata.subjectCode,
//        opeSubjectScheme = metadata.subjectScheme,
//        opeMdType = metadata.mdType,
//    )

}