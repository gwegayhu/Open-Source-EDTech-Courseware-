package world.respect.datalayer.repository.ext

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

