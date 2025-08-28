package world.respect.datalayer.db.oneroaster

import androidx.room.Transactor
import androidx.room.useWriterConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.datalayer.db.oneroaster.adapter.OneRoasterClassEntities
import world.respect.datalayer.db.oneroaster.adapter.toEntities
import world.respect.datalayer.db.oneroaster.adapter.toModel
import world.respect.datalayer.oneroster.rostering.OneRoasterDataSourceLocal
import world.respect.datalayer.oneroster.rostering.model.OneRosterClass
import world.respect.datalayer.oneroster.rostering.model.OneRosterEnrollment
import world.respect.datalayer.oneroster.rostering.model.OneRosterUser
import world.respect.datalayer.oneroster.rostering.model.composites.ClazzListDetails
import world.respect.libxxhash.XXStringHasher

class OneRoasterDataSourceDb(
    private val realmDb: RespectRealmDatabase,
    private val xxHash: XXStringHasher,
) : OneRoasterDataSourceLocal {

    override suspend fun getAllUsers(): List<OneRosterUser> {
        TODO("Not yet implemented")
    }

    override suspend fun putUser(user: OneRosterUser) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllClasses(): List<OneRosterClass> {
        TODO("Not yet implemented")
    }

    override suspend fun getClassBySourcedId(sourcedId: String): OneRosterClass {
        TODO("Not yet implemented")
    }

    override suspend fun findClassBySourcedId(
        loadParams: DataLoadParams,
        sourcedId: String
    ): DataLoadState<OneRosterClass> {
        return realmDb.getOneRoasterClassEntityDao().findClassBySourcedId(sourcedId).let {
            OneRoasterClassEntities(it)
        }.toModel().let { DataReadyState(it) }
    }

    override suspend fun findClassBySourcedIdAsFlow(guid: String): Flow<DataLoadState<OneRosterClass>> {
        TODO("Not yet implemented")
    }

    override suspend fun putClass(clazz: OneRosterClass) {
        val entities = clazz.toEntities(xxHash)

        realmDb.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                realmDb.getOneRoasterClassEntityDao().insert(entities.oneRoasterClassEntity)
            }
        }
    }

    override suspend fun getEnrolmentsByClass(classSourcedId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun putEnrollment(enrollment: OneRosterEnrollment) {
        TODO("Not yet implemented")
    }

    override suspend fun findAll(
        loadParams: DataLoadParams,
        searchQuery: String?
    ): Flow<DataLoadState<List<ClazzListDetails>>> {
        return realmDb.getOneRoasterClassEntityDao().findAllListDetailsAsFlow().map {
            DataReadyState(it)
        }
    }

}