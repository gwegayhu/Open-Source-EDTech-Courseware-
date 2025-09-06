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

class RepositoryOffsetLimitPagingSource<T: Any>(
    internal val local: PagingSource<Int, T>,
    internal val remote: PagingSource<Int, T>,
    private val onPutLocal: (List<T>) -> Unit,
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
            remote.load(params)
            withContext(NonCancellable) {
                // Do the insert and update validation info in one go.
            }
            //if new data loaded - do the insert
        }

        return local.load(params)
    }
}