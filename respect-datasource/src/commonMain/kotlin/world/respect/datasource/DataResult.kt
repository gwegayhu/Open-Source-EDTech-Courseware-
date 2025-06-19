package world.respect.datasource

/**
 *
 */
sealed class DataResult<T: Any>(
    val status: LoadingStatus,
)

class DataLoadResult<T: Any>(
    val data: T? = null,
    status: LoadingStatus,
): DataResult<T>(status)

class DataErrorResult<T: Any>(
    val error: Throwable,
    status: LoadingStatus,
): DataResult<T>(status)

