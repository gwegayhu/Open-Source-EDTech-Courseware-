package world.respect.datalayer.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import world.respect.datalayer.DataErrorResult
import world.respect.datalayer.DataLoadMetaInfo
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataLoadingState
import world.respect.datalayer.NoDataLoadedState

fun <T: Any> DataLoadState<T>.combineWithRemote(
    remote: DataLoadState<*>
): DataLoadState<T> {
    return this.copyLoadState(
        metaInfo = metaInfo.copy(
            url = remote.metaInfo.url
        ),
        localState = this,
        remoteState = remote
    )
}

fun <T: Any> Flow<DataLoadState<T>>.combineWithRemote(
    remoteFlow: Flow<DataLoadState<*>>,
): Flow<DataLoadState<T>> =combine(remoteFlow) { local, remote ->
    local.combineWithRemote(remote)
}

fun <T: Any> DataLoadState<T>.copyLoadState(
    metaInfo: DataLoadMetaInfo = this.metaInfo,
    localState: DataLoadState<T>? = this.localState,
    remoteState: DataLoadState<*>? = this.remoteState,
) : DataLoadState<T> {
    return when(this) {
        is DataReadyState -> copy(
            metaInfo = metaInfo,
            localState = localState,
            remoteState = remoteState,
        )
        is DataLoadingState -> copy(
            metaInfo = metaInfo,
            localState = localState,
            remoteState = remoteState,
        )
        is DataErrorResult -> copy(
            metaInfo = metaInfo,
            localState = localState,
            remoteState = remoteState,
        )
        is NoDataLoadedState -> copy(
            metaInfo = metaInfo,
            localState = localState,
            remoteState = remoteState,
        )
    }
}

fun <T: Any> DataLoadState<T>.dataOrNull(): T? {
    return (this as? DataReadyState)?.data
}


/**
 *  A DataReadyState may be provided when local data is available, however update checks are still
 *  going on in the background. In the case of an edit screen, we may want to show local data to
 *  a user but not allow editing until the remote data is check is done (if possible).
 *
 *  @return true if the DataLoadState is DataReadyState and there are no pending remote updates.
 */
fun DataLoadState<*>.isReadyAndSettled(): Boolean {
    return this is DataReadyState && this.remoteState !is DataLoadingState
}

fun <T: Any, R: Any> DataLoadState<T>.map(
    transform: (T) -> R
): DataLoadState<R> {
    return when(this) {
        is DataReadyState -> {
            DataReadyState(
                data = transform(data),
                metaInfo = metaInfo,
                localState = localState?.map(transform),
                remoteState = remoteState,
            )
        }
        is DataLoadingState -> {
            DataLoadingState(
                metaInfo = metaInfo,
                localState = localState?.map(transform),
                remoteState = remoteState,
            )
        }
        is DataErrorResult -> {
            DataErrorResult(
                error = error,
                metaInfo = metaInfo,
                localState = localState?.map(transform),
                remoteState = remoteState,
            )
        }
        is NoDataLoadedState -> {
            NoDataLoadedState(
                reason = reason,
                metaInfo = metaInfo,
                localState = localState?.map(transform),
                remoteState = remoteState,
            )
        }
    }
}

