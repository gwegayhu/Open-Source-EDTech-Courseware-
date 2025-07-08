package world.respect.datasource.db.opds

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.json.Json
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadState
import world.respect.datasource.db.RespectDatabase
import world.respect.datasource.opds.OpdsDataSource
import world.respect.datasource.opds.model.OpdsFeed
import world.respect.datasource.opds.model.OpdsPublication

class OpdsDataSourceDb(
    private val respectDatabase: RespectDatabase,
    private val json: Json,
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