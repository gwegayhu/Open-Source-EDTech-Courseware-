package world.respect.datalayer.db.realmdirectory.adapters

import world.respect.datalayer.db.realmdirectory.entities.RealmEntity
import world.respect.datalayer.db.shared.adapters.asEntities
import world.respect.datalayer.db.shared.adapters.toModel
import world.respect.datalayer.db.shared.entities.LangMapEntity
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.libxxhash.XXStringHasher

data class RespectRealmEntities(
    val realm: RealmEntity,
    val langMapEntities: List<LangMapEntity>,
)

fun RespectRealm.asEntity(
    xxStringHasher: XXStringHasher,
): RespectRealmEntities {
    val reUid = xxStringHasher.hash(self.toString())
    return RespectRealmEntities(
        realm = RealmEntity(
            reUid = xxStringHasher.hash(self.toString()),
            reSelf = self,
            reXapi = xapi,
            reOneRoster = oneRoster,
            reRespectExt = respectExt,
        ),
        langMapEntities = name.asEntities(
            lmeTopParentType = LangMapEntity.TopParentType.RESPECT_REALM,
            lmeTopParentUid1 = reUid,
            lmePropType = LangMapEntity.PropType.RESPECT_REALM_NAME,
            lmePropFk = 0,
        )
    )
}

fun RespectRealmEntities.toModel() : RespectRealm {
    return RespectRealm(
        self = realm.reSelf,
        xapi = realm.reXapi,
        oneRoster = realm.reOneRoster,
        respectExt = realm.reRespectExt,
        name = langMapEntities.toModel(),
    )
}
