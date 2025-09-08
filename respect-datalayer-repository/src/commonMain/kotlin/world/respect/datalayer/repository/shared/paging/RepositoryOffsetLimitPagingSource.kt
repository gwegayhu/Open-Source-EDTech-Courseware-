package world.respect.datalayer.repository.shared.paging

import androidx.paging.PagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import world.respect.datalayer.shared.paging.FilterPagingSource
import world.respect.datalayer.shared.paging.CacheableHttpPagingSource
import io.github.aakira.napier.Napier
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.ext.dataOrNull

/**
 * @param onUpdateLocalFromRemote function (e.g. provided by LocalModeDataSource) that will store
 *        newly received remote data in the local datasource.
 */
class RepositoryOffsetLimitPagingSource<T: Any>(
    internal val local: PagingSource<Int, DataLoadState<T>>,
    internal val remote: PagingSource<Int, DataLoadState<T>>,
    private val onUpdateLocalFromRemote: suspend (List<T>) -> Unit,
    tag: String? = null,
) : FilterPagingSource<Int, DataLoadState<T>>(
    src = local,
    tag = tag,
){

    val scope = CoroutineScope(Dispatchers.Default + Job())

    init {
        this.registerInvalidatedCallback { scope.cancel() }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataLoadState<T>> {
        Napier.d("RepositoryOffsetLimitPagingSource: tag=$tag load key=${params.key}")

        scope.launch {
            val remoteLoadResult = remote.load(params)

            withContext(NonCancellable) {
                if(remoteLoadResult is LoadResult.Page) {
                    onUpdateLocalFromRemote(remoteLoadResult.data.mapNotNull { it.dataOrNull() })
                }

                val isNotModifiedResponse = (remoteLoadResult as? LoadResult.Error)?.throwable is
                        CacheableHttpPagingSource.NotModifiedNonException

                if(remoteLoadResult is LoadResult.Page || isNotModifiedResponse) {
                    @Suppress("UNCHECKED_CAST")
                    (remote as? CacheableHttpPagingSource<Int, DataLoadState<T>>)
                        ?.onLoadResultStored(remoteLoadResult)
                }
            }
        }

        return super.load(params)
    }
}