package world.respect.datalayer.db.school.daos.xapi

object ActivityLangMapEntryDaoCommon {

    const val INTO_LANG_MAP_WHERE_INTERACTION_ENTITY_EXISTS = """
        INTO ActivityLangMapEntry(almeActivityUid, almeHash, almeLangCode, almePropName, almeValue, almeAieHash, almeLastMod)
        SELECT :almeActivityUid AS almeActivityUid,
               :almeHash AS almeHash,
               :almeLangCode AS almeLangCode,
               :almePropName AS almePropName,
               :almeValue AS almeValue,
               :almeAieHash AS almeAieHash,
               :almeLastMod AS almeLastMod
         WHERE EXISTS(SELECT 1
                        FROM ActivityInteractionEntity
                       WHERE ActivityInteractionEntity.aieActivityUid = :almeActivityUid
                         AND ActivityInteractionEntity.aieHash = :almeAieHash)
    """

}