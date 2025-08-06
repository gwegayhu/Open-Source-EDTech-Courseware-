package world.respect.datalayer.db.realmdirectory

import androidx.room.Transactor
import androidx.room.useWriterConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.db.RespectAppDatabase
import world.respect.datalayer.db.realmdirectory.adapters.asEntity
import world.respect.datalayer.db.shared.entities.LangMapEntity
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSourceLocal
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.datalayer.respect.model.RespectRealmDirectory
import world.respect.datalayer.respect.model.invite.RespectInviteInfo
import world.respect.libxxhash.XXStringHasher

class RealmDirectoryDataSourceDb(
    private val respectAppDb: RespectAppDatabase,
    private val json: Json,
    private val xxStringHasher: XXStringHasher,
): RealmDirectoryDataSourceLocal {

    override suspend fun allDirectories(): List<RespectRealmDirectory> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertRealm(realm: RespectRealm) {
        val realmUid = xxStringHasher.hash(realm.self.toString())

        respectAppDb.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                respectAppDb.getLangMapEntityDao().deleteByTableAndTopParentType(
                    lmeTopParentType = LangMapEntity.TopParentType.RESPECT_REALM.id,
                    lmeEntityUid1 = realmUid,
                )

                val realmEntities = realm.asEntity(xxStringHasher)
                respectAppDb.getRealmEntityDao().upsert(realmEntities.realm)
                respectAppDb.getLangMapEntityDao().insertAsync(realmEntities.langMapEntities)
            }
        }
    }

    override suspend fun allRealmsInDirectory(): List<RespectRealm> {
        TODO("Not yet implemented")
    }

    override suspend fun searchRealms(text: String): Flow<DataLoadState<List<RespectRealm>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getInviteInfo(inviteCode: String): RespectInviteInfo {
        TODO("Not yet implemented")
    }
}