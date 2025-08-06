package world.respect.datalayer.db.realmdirectory

import androidx.room.Transactor
import androidx.room.useWriterConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import world.respect.datalayer.DataLoadMetaInfo
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.db.RespectAppDatabase
import world.respect.datalayer.db.realmdirectory.adapters.toEntities
import world.respect.datalayer.db.realmdirectory.entities.RealmConfigEntity
import world.respect.datalayer.db.shared.entities.LangMapEntity
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSourceLocal
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.datalayer.respect.model.RespectRealmDirectory
import world.respect.datalayer.respect.model.invite.RespectInviteInfo
import world.respect.libxxhash.XXStringHasher
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class RealmDirectoryDataSourceDb(
    private val respectAppDb: RespectAppDatabase,
    private val json: Json,
    private val xxStringHasher: XXStringHasher,
): RealmDirectoryDataSourceLocal {

    override suspend fun allDirectories(): List<RespectRealmDirectory> {
        TODO("Not yet implemented")
    }

    override suspend fun getServerManagedDirectory(): RespectRealmDirectory? {
        TODO()
    }

    override suspend fun addServerManagedRealm(
        realm: RespectRealm,
        dbUrl: String,
    ) {
        respectAppDb.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                upsertRealm(
                    realm = DataReadyState(
                        realm,
                        DataLoadMetaInfo(
                            lastModified = Clock.System.now().toEpochMilliseconds()
                        )
                    ),
                    directory = null
                )

                respectAppDb.getRealmConfigEntityDao().upsert(
                    RealmConfigEntity(
                        rcUid = xxStringHasher.hash(realm.self.toString()),
                        dbUrl = dbUrl,
                    )
                )
            }
        }
    }

    override suspend fun upsertRealm(
        realm: DataReadyState<RespectRealm>,
        directory: RespectRealmDirectory?
    ) {
        val realmUid = xxStringHasher.hash(realm.data.self.toString())

        respectAppDb.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                respectAppDb.getLangMapEntityDao().deleteByTableAndTopParentType(
                    lmeTopParentType = LangMapEntity.TopParentType.RESPECT_REALM.id,
                    lmeEntityUid1 = realmUid,
                )

                val realmEntities = realm.toEntities(xxStringHasher)
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