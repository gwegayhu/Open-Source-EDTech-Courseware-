package world.respect.datasource.repository.opds

import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadState
import world.respect.datasource.opds.OpdsDataSource
import world.respect.datasource.opds.model.OpdsFeed
import world.respect.datasource.opds.model.OpdsPublication

class OpdsDataSourceRepository(
    private val local: OpdsDataSource,
    private val remote: OpdsDataSource,
): OpdsDataSource {

    override fun loadOpdsFeed(
        url: Url,
        params: DataLoadParams
    ): Flow<DataLoadState<OpdsFeed>> {
        return remote.loadOpdsFeed(url, params)
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
