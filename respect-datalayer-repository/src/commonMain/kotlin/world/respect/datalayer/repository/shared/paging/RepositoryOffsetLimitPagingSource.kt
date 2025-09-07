package world.respect.datalayer.repository.shared.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import world.respect.datalayer.shared.paging.FilterPagingSource
import world.respect.datalayer.shared.paging.CacheableHttpPagingSource

/**
 * @param onUpdateLocalFromRemote function (e.g. provided by LocalModeDataSource) that will store
 *        newly received remote data in the local datasource.
 */
class RepositoryOffsetLimitPagingSource<T: Any>(
    internal val local: PagingSource<Int, T>,
    internal val remote: PagingSource<Int, T>,
    private val onUpdateLocalFromRemote: suspend (List<T>) -> Unit,
) : FilterPagingSource<Int, T>(local){

    val scope = CoroutineScope(Dispatchers.Default + Job())

    init {
        this.registerInvalidatedCallback { scope.cancel() }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return local.getRefreshKey(state)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        scope.launch {
            val remoteLoadResult = remote.load(params)

            withContext(NonCancellable) {
                if(remoteLoadResult is LoadResult.Page) {
                    onUpdateLocalFromRemote(remoteLoadResult.data)
                    @Suppress("UNCHECKED_CAST")
                    (remote as? CacheableHttpPagingSource<Int, T>)?.onLoadResultStored(remoteLoadResult)
                }
            }
        }

        return local.load(params)
    }
}