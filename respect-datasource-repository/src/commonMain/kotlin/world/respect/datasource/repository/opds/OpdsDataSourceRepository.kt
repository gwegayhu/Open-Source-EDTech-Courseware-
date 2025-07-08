package world.respect.datasource.repository.opds

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
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
        url: String,
        params: DataLoadParams
    ): Flow<DataLoadState<OpdsFeed>> {
        return emptyFlow()
    }

    override fun loadOpdsPublication(
        url: String,
        params: DataLoadParams,
        referrerUrl: String?,
        expectedPublicationId: String?
    ): Flow<DataLoadState<OpdsPublication>> {
        return emptyFlow()
    }
}