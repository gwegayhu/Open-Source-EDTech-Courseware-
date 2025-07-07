package world.respect.datasource.repository.ext

import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataLoadState

data class RemoteUpdatedResult<T: Any>(
    val updatedRemoteData: DataLoadResult<T>?
)

fun <T: Any> DataLoadState<T>.checkIsRemoteUpdated(
    remote: DataLoadState<T>
): RemoteUpdatedResult<T> {
    return RemoteUpdatedResult(
        updatedRemoteData = if(this is DataLoadResult && remote is DataLoadResult &&
            remote.data != null &&
            this.metaInfo.lastModified < remote.metaInfo.lastModified
        ) {
            remote
        } else {
            null
        }
    )
}
