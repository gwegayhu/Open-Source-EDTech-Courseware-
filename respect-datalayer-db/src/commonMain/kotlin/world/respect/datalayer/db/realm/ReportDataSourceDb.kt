package world.respect.datalayer.db.realm

import androidx.room.Transactor
import androidx.room.useWriterConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.NoDataLoadedState
import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.datalayer.db.realm.adapters.toReportEntity
import world.respect.datalayer.db.realm.adapters.toRespectReport
import world.respect.datalayer.realm.ReportDataSource
import world.respect.datalayer.respect.model.RespectReport

class ReportDataSourceDb(
    private val realmDb: RespectRealmDatabase
) : ReportDataSource {

    override suspend fun allReportsAsFlow(template: Boolean): Flow<DataLoadState<List<RespectReport>>> {
        return realmDb.getReportEntityDao().getAllReportsByTemplate(template)
            .map { reportEntities ->
                DataReadyState(reportEntities.map { it.toRespectReport() })
            }
    }

    override suspend fun getReportAsync(
        loadParams: DataLoadParams,
        reportId: String
    ): DataLoadState<RespectReport> {
        val reportEntity = realmDb.getReportEntityDao().getReportAsync(reportId)
        return if (reportEntity != null) {
            DataReadyState(reportEntity.toRespectReport())
        } else {
            NoDataLoadedState(NoDataLoadedState.Reason.NOT_FOUND)
        }
    }

    override suspend fun getReportAsFlow(reportId: String): Flow<DataLoadState<RespectReport>> {
        return realmDb.getReportEntityDao().getReportAsFlow(reportId).map { reportEntity ->
            if (reportEntity != null) {
                DataReadyState(reportEntity.toRespectReport())
            } else {
                NoDataLoadedState(NoDataLoadedState.Reason.NOT_FOUND)
            }
        }
    }

    override suspend fun putReport(report: RespectReport) {
        val reportEntity = report.toReportEntity()
        realmDb.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                realmDb.getReportEntityDao().putReport(reportEntity)
            }
        }
    }

    override suspend fun updateReport(report: RespectReport) {
        val reportEntity = report.toReportEntity()
        realmDb.useWriterConnection { con ->
            con.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                realmDb.getReportEntityDao().updateReport(reportEntity)
            }
        }
    }
}