package world.respect.datasource

/**
 * Wrapper interface for data loading states; covers both remote and local database scenarios.
 *
 * @param metaInfo Loading status of the data
 */
sealed interface DataLoadState<T: Any> {
    val metaInfo: DataLoadMetaInfo
}

data class DataLoadingState<T: Any>(
    override val metaInfo: DataLoadMetaInfo = DataLoadMetaInfo(status = LoadingStatus.LOADING)
): DataLoadState<T>

data class DataLoadResult<T: Any>(
    val data: T? = null,
    override val metaInfo: DataLoadMetaInfo = DataLoadMetaInfo(status = LoadingStatus.LOADED)
): DataLoadState<T>

class DataErrorResult<T: Any>(
    val error: Throwable,
    override val metaInfo: DataLoadMetaInfo = DataLoadMetaInfo(status = LoadingStatus.FAILED)
): DataLoadState<T>

