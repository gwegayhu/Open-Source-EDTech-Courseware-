package world.respect.datalayer.shared.paging

import kotlinx.serialization.Serializable

/**
 * A RepositoryPagingSource ( e.g. RepositoryOffsetLimitPagingSource ) needs to be able to fill
 * in placeholders where placeholders are enabled and an item has not yet been loaded into the
 * local database.
 */
@Serializable
data class PagedItemHolder<T: Any>(
    val item: T?,
)
