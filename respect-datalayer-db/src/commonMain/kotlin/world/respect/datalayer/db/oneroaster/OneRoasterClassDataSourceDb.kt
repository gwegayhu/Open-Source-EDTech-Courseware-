package world.respect.datalayer.db.oneroaster

import androidx.room.Transactor
import androidx.room.useWriterConnection
import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.datalayer.db.oneroaster.adapter.toEntities
import world.respect.datalayer.oneroster.rostering.model.OneRosterClass
import world.respect.libxxhash.XXStringHasher

class OneRoasterClassDataSourceDb(
    private val realmDb: RespectRealmDatabase,
    private val xxHash: XXStringHasher,
) {

     suspend fun putClass(clazz: OneRosterClass) {
        val entities = clazz.toEntities(xxHash)

        realmDb.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                realmDb.getOneRoasterClassEntityDao().insert(entities.oneRoasterClassEntity)
            }
        }
    }

}