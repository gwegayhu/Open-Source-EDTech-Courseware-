package world.respect.app.ds

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import world.respect.datalayer.dclazz.DClazzDataSource
import world.respect.datalayer.dclazz.model.DClazz

class MockDClazzDataSource: DClazzDataSource {

    override fun allClazzesAsList(): Flow<List<DClazz>> {
        return flowOf(
            listOf(
                DClazz("third", "Third Grade", "Third Grade Class")
            )
        )
    }
}