package world.respect.datasource.repository.opds

import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataReadyState
import world.respect.datasource.DataLoadState
import world.respect.datasource.opds.OpdsDataSource
import world.respect.datasource.opds.OpdsDataSourceLocal
import world.respect.datasource.opds.model.OpdsFeed
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.datasource.repository.ext.combineWithRemote

class OpdsDataSourceRepository(
    private val local: OpdsDataSourceLocal,
    private val remote: OpdsDataSource,
): OpdsDataSource {

    override fun loadOpdsFeed(
        url: Url,
        params: DataLoadParams
    ): Flow<DataLoadState<OpdsFeed>> {
        val remoteFeed = remote.loadOpdsFeed(url, params).onEach { loadState ->
            if(loadState is DataReadyState) {
                local.updateOpdsFeed(loadState)
            }
        }

        return local.loadOpdsFeed(url, params).combine(remoteFeed) { local, remote ->
            local.combineWithRemote(remote)
        }
    }

    override fun loadOpdsPublication(
        url: Url,
        params: DataLoadParams,
        referrerUrl: Url?,
        expectedPublicationId: String?
    ): Flow<DataLoadState<OpdsPublication>> {
        return remote.loadOpdsPublication(url, params, referrerUrl, expectedPublicationId)
    }
}
