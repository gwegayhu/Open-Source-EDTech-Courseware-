package world.respect.datalayer.respect

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import world.respect.datalayer.DataErrorResult
import world.respect.datalayer.DataLoadMetaInfo
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.respect.model.RespectReport

class MockRespectReportDataSource : RespectReportDataSource {
    private val mockReports = mutableListOf(
        RespectReport(
            reportId = "0",
            title = "Weekly Session Duration",
            reportOptions = """{"title":"Weekly Session Duration","xAxis":"WEEK","period":{"type":"RelativeRangeReportPeriod","rangeUnit":"WEEK","rangeQuantity":1},"series":[{"reportSeriesUid":1,"reportSeriesTitle":"Total Duration","reportSeriesYAxis":"TOTAL_DURATION","reportSeriesVisualType":"BAR_CHART","reportSeriesSubGroup":"GENDER"}]}""",
            reportIsTemplate = false
        ),
        RespectReport(
            reportId = "1",
            title = "Weekly Session Duration",
            reportOptions = """{"title":"Weekly Session Duration","xAxis":"WEEK","period":{"type":"RelativeRangeReportPeriod","rangeUnit":"WEEK","rangeQuantity":1},"series":[{"reportSeriesUid":1,"reportSeriesTitle":"Total Duration","reportSeriesYAxis":"TOTAL_DURATION","reportSeriesVisualType":"BAR_CHART","reportSeriesSubGroup":"GENDER"}]}""",
            reportIsTemplate = false
        )

    )

    override suspend fun putReport(report: RespectReport) {
        val existingIndex = mockReports.indexOfFirst { it.reportId == report.reportId }
        if (existingIndex >= 0) {
            // Update existing report
            mockReports[existingIndex] = report
        } else {
            // Add new report with generated ID if needed
            val reportToAdd = if (report.reportId.isBlank()) {
                report.copy(reportId = (mockReports.size + 1).toString())
            } else {
                report
            }
            mockReports.add(reportToAdd)
        }
    }

    override fun getAllReportsAsFlow(): Flow<DataLoadState<RespectReport>> {
        return flowOf(
            DataReadyState(
                data = RespectReport(
                    reportId = "all",
                    title = "All Reports Combined",
                    reportOptions = """{"reports": ${mockReports.map { it.reportId }}}""",
                    reportIsTemplate = false
                ),
                metaInfo = DataLoadMetaInfo()
            )
        )
    }

    override fun getReportAsFlow(reportId: String): Flow<DataLoadState<RespectReport>> {
        val report = mockReports.firstOrNull { it.reportId == reportId }
        return flowOf(
            if (report != null) {
                DataReadyState(
                    data = report,
                    metaInfo = DataLoadMetaInfo()
                )
            } else {
                DataErrorResult(
                    error = NoSuchElementException("Report not found"),
                    metaInfo = DataLoadMetaInfo()
                )
            }
        )
    }
}