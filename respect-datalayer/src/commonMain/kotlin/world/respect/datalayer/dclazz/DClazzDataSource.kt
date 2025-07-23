package world.respect.datalayer.dclazz

import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.dclazz.model.DClazz

interface DClazzDataSource {

    fun allClazzesAsList(): Flow<List<DClazz>>

}