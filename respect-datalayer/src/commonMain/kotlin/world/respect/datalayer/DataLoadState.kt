package world.respect.datalayer

import kotlinx.serialization.Serializable

/**
 * Wrapper interface for the state of data being loaded. Allows a data layer flow to contain
 * both the data and the loading state.
 *
 * @property metaInfo metadata about the data being loaded e.g. URL, last modified info, etc.
 * @property localState the state of the data as loaded from the local data source, if applicable.
 *           This is set by the Repository when there is a distinct local and network result.
 * @property remoteState the state of the data as loaded from the network data source, if applicable.
 *           This is set by the Repository when there is a distinct local and network result.
 */

@Serializable
sealed interface DataLoadState<T: Any> {
    val metaInfo: DataLoadMetaInfo
    val localState: DataLoadState<T>?
    val remoteState: DataLoadState<*>?
}

/**
 * Data loading is in progress
 */
@Serializable
data class DataLoadingState<T: Any>(
    override val metaInfo: DataLoadMetaInfo = DataLoadMetaInfo(),
    override val localState: DataLoadState<T>? = null,
    override val remoteState: DataLoadState<*>? = null,
): DataLoadState<T>

/**
 * Data has not been loaded and this is not an error - e.g. when a remote datasource returns 304
 * not modified, no content, or when the local datasource is empty.
 */
@Serializable
data class NoDataLoadedState<T: Any>(
    val reason: Reason,
    override val metaInfo: DataLoadMetaInfo = DataLoadMetaInfo(),
    override val localState: DataLoadState<T>? = null,
    override val remoteState: DataLoadState<*>? = null,
): DataLoadState<T> {

    enum class Reason {
        NOT_MODIFIED, NOT_FOUND
    }

    companion object {

        @Suppress("unused") //reserved for future use
        fun <T: Any> notModified(
            metaInfo: DataLoadMetaInfo = DataLoadMetaInfo(),
        ): NoDataLoadedState<T> = NoDataLoadedState(Reason.NOT_MODIFIED)

        @Suppress("unused") //reserved for future use
        fun <T: Any> notFound(
            metaInfo: DataLoadMetaInfo = DataLoadMetaInfo(),
        ): NoDataLoadedState<T> = NoDataLoadedState(Reason.NOT_FOUND)

    }

}

/**
 * Data is loaded and ready. If loaded from a repository, it may be that the local data is ready
 * for display and the remote data is still being loaded.
 */
@Serializable
data class DataReadyState<T: Any>(
    val data: T,
    override val metaInfo: DataLoadMetaInfo = DataLoadMetaInfo(),
    override val localState: DataLoadState<T>? = null,
    override val remoteState: DataLoadState<*>? = null,
): DataLoadState<T>

/**
 * There has been an error loading data.
 */
data class DataErrorResult<T: Any>(
    val error: Throwable,
    override val metaInfo: DataLoadMetaInfo = DataLoadMetaInfo(),
    override val localState: DataLoadState<T>? = null,
    override val remoteState: DataLoadState<*>? = null,
): DataLoadState<T>

