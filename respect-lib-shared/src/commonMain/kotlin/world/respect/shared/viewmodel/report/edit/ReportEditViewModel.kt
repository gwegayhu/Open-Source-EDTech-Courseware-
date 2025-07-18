package world.respect.shared.viewmodel.report.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import world.respect.datalayer.db.shared.entities.Report
import world.respect.shared.domain.report.model.RelativeRangeReportPeriod
import world.respect.shared.domain.report.model.ReportOptions
import world.respect.shared.domain.report.model.ReportSeries
import world.respect.shared.domain.report.model.ReportSeriesVisualType
import world.respect.shared.domain.report.model.ReportSeriesYAxis
import world.respect.shared.ext.replace
import world.respect.shared.navigation.NavCommand
import world.respect.shared.navigation.ReportDetail
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.shared.viewmodel.app.appstate.AppUiState
import world.respect.shared.viewmodel.app.appstate.LoadingUiState

data class ReportEditUiState(
    val reportOptions: ReportOptions = ReportOptions(),
    val reportTitleError: String? = null,
    val xAxisError: String? = null,
    val seriesTitleErrors: Map<Int, String> = emptyMap(),
    val yAxisErrors: Map<Int, String> = emptyMap(),
    val subGroupError: String? = null,
    val chartTypeError: Map<Int, String> = emptyMap(),
    val timeRangeError: String? = null,
    val quantityError: String? = null,
    val submitted: Boolean = false,
    val hasSingleSeries: Boolean = true,
)

class ReportEditViewModel(
    savedStateHandle: SavedStateHandle,
) : RespectViewModel(savedStateHandle) {

    // TODO: Initialize with proper dependency injection
    private val _uiState: MutableStateFlow<ReportEditUiState> =
        MutableStateFlow(ReportEditUiState())
    val uiState: Flow<ReportEditUiState> = _uiState.asStateFlow()

    // TODO: Get actual entity UID from navigation arguments
    private val entityUid: Long = 0
    private var nextTempFilterUid = -1
    private val entityUidArg: Long = 0

    init {
        loadingState = LoadingUiState.INDETERMINATE
        // TODO: Replace with string resources
        val title = if (entityUid == 0L) {
            "add_a_new_report"
        } else {
            "edit_report"
        }

        _appUiState.update {
            AppUiState(
                title = title,
                hideBottomNavigation = false
            )
        }

        // TODO: Replace with actual report loading logic
        val mockedReport = ReportOptions()
        _uiState.update { prev ->
            prev.copy(
                reportOptions = mockedReport
            )
        }
    }

    fun onClickSave() {
        // TODO: Move validation messages to string resources
        val requiredFieldMessage = "field_required_prompt"
        val currentReport = _uiState.value.reportOptions

        // Validate quantity if timeRange is RelativeRangeReportPeriod
        val quantityError = if (currentReport.period is RelativeRangeReportPeriod) {
            val qty = (currentReport.period).rangeQuantity
            if (qty < 1) {"quantity_must_be_at_least_1"} else {null}
        } else {
            null
        }

        // Validate series-specific fields
        val seriesTitleErrors = currentReport.series.mapNotNull { series ->
            series.reportSeriesTitle.takeIf { it.isEmpty() }
                ?.let { series.reportSeriesUid to requiredFieldMessage }
        }.toMap()

        val yAxisErrors = currentReport.series.mapNotNull { series ->
            if (series.reportSeriesYAxis == null) {
                series.reportSeriesUid to requiredFieldMessage
            } else {
                null
            }
        }.toMap()

        val chartTypeErrors = currentReport.series.mapNotNull { series ->
            if (series.reportSeriesVisualType == null) {
                series.reportSeriesUid to requiredFieldMessage
            } else {
                null
            }
        }.toMap()

        // Validate all fields
        val newState = _uiState.value.copy(
            submitted = true,
            reportTitleError = if (currentReport.title.isEmpty()) requiredFieldMessage else null,
            xAxisError = null,
            timeRangeError = null,
            quantityError = quantityError,
            chartTypeError = chartTypeErrors,
            yAxisErrors = yAxisErrors,
            seriesTitleErrors = seriesTitleErrors
        )

        _uiState.value = newState

        if (newState.hasErrors()) {
            loadingState = LoadingUiState.NOT_LOADING
            // TODO: Add analytics for validation errors
            println("Report options error")
            return
        }

        viewModelScope.launch {
            try {
                val currentReports = _uiState.value.reportOptions
                val report = Report(
                    reportUid = entityUidArg,
                    reportTitle = currentReports.title,
                    reportOptions = Json.encodeToString(currentReports),
                    reportOwnerPersonUid = 0L, // TODO: Get actual user ID
                )

                // TODO: Implement proper repository pattern
                if (entityUidArg == 0L) {
//                    activeRepoWithFallback.reportDao().insertAsync(report)
                    println("Report options inserted successfully: ${report}")
                } else {
//                    activeRepoWithFallback.reportDao().updateAsync(report)
                    println("Report options updated successfully: ${report}")
                }

                // TODO: Add success feedback to user
                // TODO: Navigate back to report detail view
            } catch (e: Exception) {
                // TODO: Add proper error handling and user feedback
                println("Error updating report options: ${e.message}")
                // TODO: Add analytics for error tracking
            } finally {
                _navCommandFlow.tryEmit(
                    NavCommand.Navigate(
                        ReportDetail
                    )
                )
            }
        }
    }

    fun onEntityChanged(newOptions: ReportOptions) {
        // TODO: Move validation messages to string resources
        val requiredFieldMessage = "field_required_prompt"

        val quantityError = when (val timeRange = newOptions.period) {
            is RelativeRangeReportPeriod -> {
                if (timeRange.rangeQuantity < 1) "quantity_must_be_at_least_1" else null
            }
            else -> null
        }

        val seriesTitleErrors = newOptions.series.associate { series ->
            series.reportSeriesUid to if (series.reportSeriesTitle.isEmpty()) requiredFieldMessage else null
        }.filterValues { it != null }
            .mapValues { it.value as String }

        val yAxisErrors = newOptions.series.associate { series ->
            series.reportSeriesUid to if (series.reportSeriesYAxis == null) requiredFieldMessage else null
        }.filterValues { it != null }
            .mapValues { it.value as String }

        val chartTypeErrors = newOptions.series.associate { series ->
            series.reportSeriesUid to if (series.reportSeriesVisualType == null) requiredFieldMessage else null
        }.filterValues { it != null }
            .mapValues { it.value as String }

        _uiState.update { currentState ->
            val baseUpdate = currentState.copy(
                reportOptions = newOptions,
                hasSingleSeries = newOptions.series.size == 1
            )

            if (!baseUpdate.submitted) return@update baseUpdate

            baseUpdate.copy(
                reportTitleError = if (newOptions.title.isEmpty()) "field_required_prompt" else null,
                xAxisError = if (newOptions.xAxis == null) "field_required_prompt" else null,
                seriesTitleErrors = seriesTitleErrors,
                yAxisErrors = yAxisErrors,
                timeRangeError = null,
                quantityError = quantityError,
                chartTypeError = chartTypeErrors,
            )
        }
    }

    fun onSeriesChanged(updatedSeries: ReportSeries) {
        onEntityChanged(
            _uiState.value.reportOptions.let { reportOptions ->
                reportOptions.copy(
                    series = reportOptions.series.replace(updatedSeries) {
                        it.reportSeriesUid == updatedSeries.reportSeriesUid
                    }
                )
            }
        )
    }

    fun onAddSeries() {
        _uiState.update { prev ->
            val newUid = (prev.reportOptions.series.maxOfOrNull { it.reportSeriesUid } ?: 0) + 1
            prev.copy(
                reportOptions = prev.reportOptions.copy(
                    series = prev.reportOptions.series + ReportSeries(
                        reportSeriesUid = newUid,
                        reportSeriesTitle = "Series $newUid", // TODO: Use string resource
                        reportSeriesVisualType = ReportSeriesVisualType.BAR_CHART, // Default type
                        reportSeriesSubGroup = null,
                        reportSeriesYAxis = ReportSeriesYAxis.TOTAL_DURATION // Default Y-axis
                    ),
                ),
                hasSingleSeries = false
            )
        }
    }

    fun onRemoveSeries(seriesId: Int) {
        _uiState.update { prev ->
            val updatedSeriesList =
                prev.reportOptions.series.filterNot { it.reportSeriesUid == seriesId }
            prev.copy(
                reportOptions = prev.reportOptions.copy(
                    series = updatedSeriesList
                ),
                hasSingleSeries = updatedSeriesList.size == 1
            )
        }
    }

    fun ReportEditUiState.hasErrors(): Boolean {
        if (!submitted) return false
        return reportTitleError != null ||
                xAxisError != null ||
                seriesTitleErrors.isNotEmpty() ||
                yAxisErrors.isNotEmpty() ||
                subGroupError != null ||
                chartTypeError.isNotEmpty() ||
                timeRangeError != null ||
                quantityError != null
    }
}