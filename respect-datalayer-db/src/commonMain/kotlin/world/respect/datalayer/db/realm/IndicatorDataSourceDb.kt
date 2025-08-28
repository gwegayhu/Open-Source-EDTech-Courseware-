package world.respect.datalayer.db.realm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.NoDataLoadedState
import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.datalayer.db.realm.adapters.toIndicator
import world.respect.datalayer.db.realm.adapters.toIndicatorEntity
import world.respect.datalayer.realm.IndicatorDataSource
import world.respect.datalayer.respect.model.Indicator

class IndicatorDataSourceDb(
    private val realmDb: RespectRealmDatabase
): IndicatorDataSource {

    override suspend fun allIndicatorAsFlow(): Flow<DataLoadState<List<Indicator>>> {
        return realmDb.getIndicatorEntityDao().getAllIndicator().map { indicatorEntities ->
            DataReadyState(indicatorEntities.map { it.toIndicator() })
        }
    }

    override suspend fun getIndicatorAsync(
        loadParams: DataLoadParams,
        indicatorId: String
    ): DataLoadState<Indicator> {
        val indicatorEntity = realmDb.getIndicatorEntityDao().getIndicatorAsync(indicatorId)
        return if (indicatorEntity != null) {
            DataReadyState(indicatorEntity.toIndicator())
        } else {
            NoDataLoadedState(NoDataLoadedState.Reason.NOT_FOUND)
        }
    }

    override suspend fun getIndicatorAsFlow(indicatorId: String): Flow<DataLoadState<Indicator>> {
        return realmDb.getIndicatorEntityDao().getIndicatorAsFlow(indicatorId).map { indicatorEntity ->
            if (indicatorEntity != null) {
                DataReadyState(indicatorEntity.toIndicator())
            } else {
                NoDataLoadedState(NoDataLoadedState.Reason.NOT_FOUND)
            }
        }
    }

    override suspend fun putIndicator(indicator: Indicator) {
        val indicatorEntity = indicator.toIndicatorEntity()
        realmDb.getIndicatorEntityDao().putIndicator(indicatorEntity)
    }

    override suspend fun updateIndicator(indicator: Indicator) {
        val indicatorEntity = indicator.toIndicatorEntity()
        realmDb.getIndicatorEntityDao().updateIndicator(indicatorEntity)
    }
}