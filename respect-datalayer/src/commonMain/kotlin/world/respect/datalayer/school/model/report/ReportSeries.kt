package world.respect.datalayer.school.model.report

import kotlinx.datetime.DatePeriod
import kotlinx.serialization.Serializable
import world.respect.datalayer.respect.model.Indicator

@Serializable
data class ReportSeries(
    val reportSeriesUid: Int = 0,
    val reportSeriesTitle: String = "Series 1",
    val reportSeriesYAxis: Indicator = DefaultIndicators.list.first(),
    val reportSeriesVisualType: ReportSeriesVisualType = ReportSeriesVisualType.BAR_CHART,
    val reportSeriesSubGroup: ReportXAxis? = ReportXAxis.DAY,
    val reportSeriesFilters: List<ReportFilter>? = null
)

enum class YAxisTypes {
    COUNT, DURATION, PERCENTAGE
}

enum class ReportSeriesVisualType {
    BAR_CHART, LINE_GRAPH
}

enum class ReportXAxis(
    val personJoinRequired: Boolean = false,
    val datePeriod: DatePeriod? = null,
) {
    DAY(datePeriod = DatePeriod(days = 1)),
    WEEK(datePeriod = DatePeriod(days = 7)),
    MONTH(datePeriod = DatePeriod(months = 1)),
    QUARTER(datePeriod = DatePeriod(months = 3)),
    YEAR(datePeriod = DatePeriod(years = 1)),
    TIME_OF_DAY,
    CLASS,
    SUBJECT,
    SCHOOL,
    ASSESSMENT_TYPE,
    GRADE_LEVEL,
    GENDER(personJoinRequired = true),
    AGE_GROUP(personJoinRequired = true),
    REGION,
    LANGUAGE(personJoinRequired = true),
    USER_ROLE(personJoinRequired = true),
    ACTIVITY_VERB,
    APPLICATION,
    DEVICE_TYPE,
}

enum class FilterType {
    PERSON_AGE, PERSON_GENDER
}

enum class GenderType {
    MALE, FEMALE, OTHER
}
