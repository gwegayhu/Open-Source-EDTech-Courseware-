package world.respect.datasource.repository.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataLoadState

/**
 * Given a local datasource flow and a remote datasource flow, combine the two to provide an
 * offline-first datasource for the UI.
 *
 * Any locally available data can therefor displayed immediately whilst the remote datasource checks
 * for updates if/as required or possible.
 *
 * @receiver the DataLoadState flow from the local data source
 * @param remoteFlow the DataLoadState flow from the remote data source
 * @param onRemoteNewer a function that will be invoked when the remote flow data is newer than the
 *        local data e.g. to update the local datasource.
 */
fun <T: Any> Flow<DataLoadState<T>>.combineLocalWithRemote(
    remoteFlow: Flow<DataLoadState<T>>,
    onRemoteNewer: suspend (DataLoadResult<T>) -> Unit,
): Flow<DataLoadState<T>> {
    val mutex = Mutex()

    return combine(remoteFlow) { local, remote ->
        if(local is DataLoadResult && remote is DataLoadResult
            && remote.data != null
            && local.metaInfo.lastModified < remote.metaInfo.lastModified
        ) {
            mutex.withLock {
                onRemoteNewer(remote)
            }
        }

        local
    }
}