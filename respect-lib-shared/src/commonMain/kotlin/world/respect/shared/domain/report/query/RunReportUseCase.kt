package world.respect.shared.domain.report.query

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import world.respect.datalayer.db.shared.entities.ReportQueryResult
import world.respect.datalayer.http.headers.directives.directivesToMap
import world.respect.shared.domain.report.ext.asStatementReportRow
import world.respect.datalayer.school.model.report.ReportOptions
import world.respect.datalayer.school.model.report.ReportSeries
import world.respect.datalayer.school.model.report.StatementReportRow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Use case to accept request to run a report and generate report results (that can then be presented
 * as a graph). This could be implemented via:
 *
 * a) running the report directly on the database
 * b) sending the report options to the server
 * c) using a cached report result
 */
interface RunReportUseCase {

    /**
     * Data class representing report results in a format that can be turned into a graph
     *
     * @param timestamp when the report was actually run (in ms since epoch)
     * @param request the run report request that generated the result (including report options)
     * @param results data points that can be used to draw a graph. This is a list of statementreportrow
     *        lists, where the first list is in the same order as request.options.series (e.g. for each series
     *        there is a list of the results). The order of statementreportrows depends on the the
     *        xAxis (e.g. whether it is based on dates or not).
     * @param age Age as used in http headers (e.g. seconds since report was run)
     */
    @Serializable
    data class RunReportResult(
        val timestamp: Long,
        val request: RunReportRequest,
        val results: List<List<StatementReportRow>>,
        val age: Int = 0,
    ) {

        data class Series(
            val reportSeriesOptions: ReportSeries,
            val data: List<StatementReportRow>,
        )

        /**
         * A result subgroup is the combination of both the Series and a particular subgroup
         * value e.g. a user might create a report with two series, both of which are subgrouped
         * by gender. This results in 4 series/subgroup combinations e.g. Series 1 - Male,
         * Series 1 - Female, Series2 - Male, Series2 - Female.
         *
         * @property value the subgroup value (e.g. clazzUid when subgrouped by clazz, Person.gender
         * value when subgrouped by gender, etc)
         * @property series the related RunReportResult.Series
         */
        data class Subgroup(
            val value: String,
            val series: Series,
        ) {

            val subgroupData: List<StatementReportRow> by lazy {
                series.data.filter { it.subgroup == value }
            }

        }

        val resultSeries: List<Series> by lazy {
            request.reportOptions.series.mapIndexed { index, reportSeriesOptions ->
                Series(
                    reportSeriesOptions = reportSeriesOptions,
                    data = results[index],
                )
            }
        }

        val distinctSubgroups: List<Subgroup> by lazy {
            resultSeries.map { series ->
                series.data.map { it.subgroup }.distinct().map {
                    Subgroup(value = it, series = series)
                }
            }.flatten()
        }

        val distinctXAxisValueSorted: List<String> by lazy {
            results.flatten().map { it.xAxis }.distinct().sorted()
        }

        val maxYValue: Double? by lazy {
            results.maxOfOrNull { resultList ->
                resultList.maxOfOrNull { it.yAxis } ?: 0.toDouble()
            }
        }

        val yRange: ClosedFloatingPointRange<Float> by lazy {
            val maxVal = maxYValue ?: 0.toDouble()

            if (maxVal > 0) {
                0.0f..(maxVal.toFloat() * Y_RANGE_BUFFER_FACTOR)
            } else {
                0.0f..1.0f
            }
        }

        val yAxisType: String by lazy {
            request.reportOptions.series.first().reportSeriesYAxis.type
        }


        //Add functions to get info needed for graphs in a clear/logical way e.g. distinct xaxis,subgroups

    }

    /**
     * Data class representing a request to run a report.
     *
     * @param reportOptions ReportOptions as selected by user (xAxis, yAxis, time range, etc)
     * @param accountPersonUid personUid of the person running the query. This will affect the result
     *        as the data that can be accessed depends on the permissions available to the person.
     * @param cacheControl cache control - would be used to set must-revalidate, only-if-cached, etc.
     * @param timeZone TimeZone to use for date calculations - see ReportPeriod for further info on
     *        how this is used.
     */
    @Serializable
    data class RunReportRequest(
        val reportUid: Long,
        val reportOptions: ReportOptions,
        val accountPersonUid: Long,
        val cacheControl: String? = null,
        val timeZone: TimeZone,
    ) {

        /**
         * The maximum age (in seconds as per http headers) where a report will be considered fresh
         */
        val maxFreshAge: Int by lazy {
            val directives = cacheControl?.let { directivesToMap(it) }
            val maxAgeVal = directives?.get("max-age")

            when {
                directives?.containsKey("must-revalidate") == true -> 0
                maxAgeVal != null -> maxAgeVal.toInt()
                else -> DEFAULT_MAX_AGE
            }
        }
    }

    /**
     * Run the report as per options provided and return the result.
     *
     * @param request the report request to run
     * @return result containing data that can be graphed
     */
    operator fun invoke(
        request: RunReportRequest
    ): Flow<RunReportResult>

    companion object {

        /**
         * Where the XAxis is time based (day/week/month/year) then the list of StatementReportRow MUST
         * contain a row for each day/week/month for each subgroup. If there are no matching records in
         * the database, then the SQL query will not contain any such row.
         *
         * This functions "fills" it in with zero so the data can be graphed as expected.
         */
        @OptIn(ExperimentalTime::class)
        private fun List<StatementReportRow>.fillIfNeeded(
            request: RunReportRequest,
        ): List<StatementReportRow> {
            //If there are no rows in the database query result; we must use the empty subgroup
            // this might need adjusted when subgroups are by gender / known values
            val allSubGroups = this.map { it.subgroup }.distinct().ifEmpty { listOf("") }
            val datePeriod = request.reportOptions.xAxis.datePeriod ?: return this
            val resultList = mutableListOf<StatementReportRow>()
            val rowMap = this.associateBy { Pair(it.xAxis, it.subgroup) }

            var fromDateTime = Instant
                .fromEpochMilliseconds(request.reportOptions.period.periodStartMillis(request.timeZone))
                .toLocalDateTime(request.timeZone)
            val reportEndMs = request.reportOptions.period.periodEndMillis(request.timeZone)

            while (fromDateTime.toInstant(request.timeZone).toEpochMilliseconds() < reportEndMs) {
                val xAxisStr = fromDateTime.date.toString()
                resultList.addAll(
                    allSubGroups.map { subgroup ->
                        rowMap[Pair(xAxisStr, subgroup)] ?: StatementReportRow(
                            xAxis = xAxisStr,
                            subgroup = subgroup
                        )
                    }
                )

                fromDateTime = LocalDateTime(fromDateTime.date.plus(datePeriod), fromDateTime.time)
            }

            return resultList.toList()
        }

        fun reportQueryResultsToResultStatementReportRows(
            queryResults: List<ReportQueryResult>,
            request: RunReportRequest,
            xAxisNameFn: (String) -> String = { it },
        ): List<List<StatementReportRow>> {
            val queryResultMap = queryResults.groupBy { it.rqrReportSeriesUid }
                .map { entry ->
                    entry.key to entry.value.map {
                        it.asStatementReportRow().let { reportRow ->
                            reportRow.copy(
                                xAxis = xAxisNameFn(reportRow.xAxis)
                            )
                        }
                    }.fillIfNeeded(request)
                }.toMap()

            return request.reportOptions.series.mapNotNull {
                //ensure that the order matches the order as per request.reportOptions.series
                queryResultMap[it.reportSeriesUid]
            }
        }


        const val DEFAULT_MAX_AGE = (60 * 60)//one hour

        /**
         * When displaying a graph and providing the max y axis value to the graphing library,
         * add a buffer factor for space (10% extra)
         */
        const val Y_RANGE_BUFFER_FACTOR = 1.1f


    }
}