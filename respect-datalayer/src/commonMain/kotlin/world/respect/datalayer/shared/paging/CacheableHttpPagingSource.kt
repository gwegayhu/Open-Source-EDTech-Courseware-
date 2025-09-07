package world.respect.datalayer.shared.paging

import androidx.paging.PagingSource

/**
 * As per BaseDataSourceValidationHelper there are two potential implementations of data layer
 * caching: where there is a 1:1 relationship between the URL loaded and entities stored in the
 * database, and an extended one where this is not the case.
 *
 * In the extended case the validation ( ExtendedDataSourceValidationHelper ), the validation info
 * must be updated after the data from the request has been successfully stored.
 *
 */
interface CacheableHttpPagingSource<Key: Any, Value: Any> {

    suspend fun onLoadResultStored(
        loadResult: PagingSource.LoadResult.Page<Key, Value>
    )

}