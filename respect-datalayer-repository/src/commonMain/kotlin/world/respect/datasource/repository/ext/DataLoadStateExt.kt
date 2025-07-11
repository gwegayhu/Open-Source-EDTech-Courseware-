package world.respect.datasource.repository.ext

import world.respect.datasource.DataErrorResult
import world.respect.datasource.DataLoadMetaInfo
import world.respect.datasource.DataReadyState
import world.respect.datasource.DataLoadState
import world.respect.datasource.DataLoadingState
import world.respect.datasource.NoDataLoadedState

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

