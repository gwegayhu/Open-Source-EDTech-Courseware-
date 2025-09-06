package world.respect.datalayer.shared.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.atomicfu.atomic

/**
 * Similar concept to FilterInputStream / FilterOutputStream - will generally just pass things through. Child classes
 * can change.
 *
 * Invalidation is delegated to the src.
 *
 * This is derived from UstadMobile door's DelegatedInvalidationPagingSource and FilterPagingSource
 */
abstract class FilterPagingSource<Key: Any, Value: Any>(
    private val src: PagingSource<Key, Value>,
) : PagingSource<Key, Value>(){

    private val srcInvalidateCallbackRegistered = atomic(false)

    private val invalidated = atomic(false)

    override val jumpingSupported: Boolean
        get() = src.jumpingSupported

    override val keyReuseSupported: Boolean
        get() = src.keyReuseSupported

    private val srcInvalidatedCallback: () -> Unit = {
        onSrcInvalidated()
    }

    private fun onSrcInvalidated() {
        src.unregisterInvalidatedCallback(srcInvalidatedCallback)
        if(!invalidated.getAndSet(true)) {
            invalidate()
        }
    }

    protected fun registerInvalidationCallbackIfNeeded() {
        if(!srcInvalidateCallbackRegistered.getAndSet(true)) {
            src.registerInvalidatedCallback(srcInvalidatedCallback)
        }
    }

    override fun getRefreshKey(state: PagingState<Key, Value>): Key? {
        return src.getRefreshKey(state)
    }

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value> {
        registerInvalidationCallbackIfNeeded()
        return src.load(params)
    }


}