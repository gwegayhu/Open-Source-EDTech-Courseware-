package world.respect.shared.viewmodel.report.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.serialization.json.Json
import world.respect.datalayer.db.shared.entities.Report
import world.respect.shared.domain.report.formatter.CreateGraphFormatterUseCase
import world.respect.shared.domain.report.formatter.GraphFormatter
import world.respect.shared.domain.report.model.ReportOptions
import world.respect.shared.domain.report.model.ReportPeriodOption
import world.respect.shared.domain.report.model.ReportSeries
import world.respect.shared.domain.report.model.ReportSeriesVisualType
import world.respect.shared.domain.report.model.ReportSeriesYAxis
import world.respect.shared.domain.report.model.ReportXAxis
import world.respect.shared.domain.report.model.StatementReportRow
import world.respect.shared.domain.report.query.RunReportUseCase
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.ReportDetail
import world.respect.shared.navigation.ReportEdit
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.FabUiState
import world.respect.shared.viewmodel.app.appstate.LoadingUiState.Companion.NOT_LOADING
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class ReportListUiState(
    val reportList: () -> PagingSource<Int, Report> = { EmptyPagingSource() },
    val addSheetOrDialogVisible: Boolean = false,
    val activeUserPersonUid: Long = 0L,
    val xAxisFormatter: GraphFormatter<String>? = null,
    val yAxisFormatter: GraphFormatter<Double>? = null
)

class ReportListViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    // TODO: Implement proper dependency injection for these use cases
    // Currently unimplemented - will need to be initialized before use
    private lateinit var runReportUseCase: RunReportUseCase
    private val createGraphFormatterUseCase: CreateGraphFormatterUseCase = CreateGraphFormatterUseCase()

    private val _uiState = MutableStateFlow(ReportListUiState())
    val uiState: Flow<ReportListUiState> = _uiState.asStateFlow()
    private val activeUserPersonUid: Long = 0
    private val pagingSourceFactory: () -> PagingSource<Int, Report> = { DummyReportPagingSource() }

    init {
        _appUiState.update { prev ->
            prev.copy(
                navigationVisible = true,
                title = "reports",
                fabState = FabUiState(
                    text = "reports",
                    icon = FabUiState.FabIcon.ADD,
                    onClick = this::onClickAdd,
                )
            )
        }
        _appUiState.update { prev ->
            prev.copy(
                fabState = prev.fabState.copy(
                    visible = true
                )
            )
        }
        viewModelScope.launch {
            _uiState.update { prev ->
                prev.copy(
                    reportList = pagingSourceFactory,
                    activeUserPersonUid = activeUserPersonUid
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun runReport(report: Report): Flow<RunReportUseCase.RunReportResult> = flow {
        try {
            // TODO: Replace with actual report data fetching logic
            // This is dummy implementation - generates random data for testing
            val dummyResults = List(3) { dayIndex ->
                List(3) { seriesIndex ->
                    StatementReportRow(
                        xAxis = "Day ${dayIndex + 1}",
                        subgroup = "Group ${seriesIndex + 1}",
                        yAxis = (1..100).random().toDouble()
                    )
                }
            }

            // TODO: Replace with actual report options from database or user input
            // This is dummy implementation - creates sample options for testing
            val dummyReportOptions = ReportOptions(
                title = "Sample Report",
                xAxis = ReportXAxis.DAY,
                period = ReportPeriodOption.LAST_WEEK.period,
                series = List(3) { index ->
                    ReportSeries(
                        reportSeriesTitle = "Series ${index + 1}",
                        reportSeriesUid = index + 1,
                        reportSeriesYAxis = when (index % 3) {
                            0 -> ReportSeriesYAxis.TOTAL_DURATION
                            1 -> ReportSeriesYAxis.AVERAGE_DURATION
                            else -> ReportSeriesYAxis.NUMBER_SESSIONS
                        },
                        reportSeriesVisualType = ReportSeriesVisualType.BAR_CHART,
                        reportSeriesSubGroup = ReportXAxis.DAY
                    )
                }
            )

            val reportResult = RunReportUseCase.RunReportResult(
                timestamp = Clock.System.now().toEpochMilliseconds(),
                request = RunReportUseCase.RunReportRequest(
                    reportUid = report.reportUid,
                    reportOptions = dummyReportOptions,
                    accountPersonUid = activeUserPersonUid,
                    timeZone = TimeZone.currentSystemDefault()
                ),
                results = dummyResults
            )

            // Create formatters for the graph axes
            val xAxisFormatter = createGraphFormatterUseCase(
                reportResult = reportResult,
                options = CreateGraphFormatterUseCase.FormatterOptions(
                    paramType = String::class,
                    axis = CreateGraphFormatterUseCase.FormatterOptions.Axis.X_AXIS_VALUES
                )
            )

            val yAxisFormatter = createGraphFormatterUseCase(
                reportResult = reportResult,
                options = CreateGraphFormatterUseCase.FormatterOptions(
                    paramType = Double::class,
                    axis = CreateGraphFormatterUseCase.FormatterOptions.Axis.Y_AXIS_VALUES
                )
            )

            // Update UI state with the formatters
            _uiState.update { currentState ->
                currentState.copy(
                    xAxisFormatter = xAxisFormatter,
                    yAxisFormatter = yAxisFormatter
                )
            }

            emit(reportResult)

        } catch (e: Exception) {
            // TODO: Implement proper error handling and logging
            val errorResult = RunReportUseCase.RunReportResult(
                timestamp = Clock.System.now().toEpochMilliseconds(),
                request = RunReportUseCase.RunReportRequest(
                    reportUid = report.reportUid,
                    reportOptions = ReportOptions(), // Empty options in error case
                    accountPersonUid = activeUserPersonUid,
                    timeZone = TimeZone.currentSystemDefault()
                ),
                results = emptyList()
            )
            emit(errorResult)
            throw e
        } finally {
            _appUiState.update { it.copy(loadingState = NOT_LOADING) }
        }
    }

    fun onClickAdd() {
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                ReportEdit
            )
        )

    }

    fun onClickEntry(entry: Report) {
        // Implement entry click functionality
        _navCommandFlow.tryEmit(
            NavCommand.Navigate(
                ReportDetail
            )
        )
    }

    fun onRemoveReport(uid: Long) {
        // Implement remove functionality
    }
}


class DummyReportPagingSource : PagingSource<Int, Report>() {
    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Report> {
        // TODO: Replace with actual database query for reports
        // This is dummy implementation - creates sample reports for testing
        val dummyReports = List(3) { index ->
            Report(
                reportUid = index.toLong(),
                reportTitle = "Report ${index + 1}",
                reportOptions = Json.encodeToString(
                    ReportOptions.serializer(),
                    ReportOptions(
                        title = "Sample Report $index",
                        series = listOf(
                            ReportSeries(
                                reportSeriesTitle = "Data Series",
                                reportSeriesUid = 1,
                                reportSeriesYAxis = ReportSeriesYAxis.TOTAL_DURATION,
                                reportSeriesVisualType = ReportSeriesVisualType.BAR_CHART
                            )
                        )
                    )
                ),
                reportIsTemplate = index % 3 == 0,
                reportLastModTime = System.currentTimeMillis() - (index * 86400000L),
                reportOwnerPersonUid = 1L
            )
        }

        return _root_ide_package_.app.cash.paging.PagingSourceLoadResultPage(
            data = dummyReports,
            prevKey = null,
            nextKey = null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Report>): Int? = null
}

class EmptyPagingSource<Key : Any, Value : Any> : PagingSource<Key, Value>() {
    // TODO: Consider if this is needed in production or just for testing
    override fun getRefreshKey(state: PagingState<Key, Value>): Key? = null

    override suspend fun load(params: PagingSourceLoadParams<Key>): PagingSourceLoadResult<Key, Value> {
        return _root_ide_package_.app.cash.paging.PagingSourceLoadResultPage<Key, Value>(
            emptyList(),
            null,
            null
        ) as PagingSourceLoadResult<Key, Value>
    }
}