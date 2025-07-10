package world.respect.datasource.opds

import world.respect.datasource.DataLoadResult
import world.respect.datasource.opds.model.OpdsFeed
import world.respect.datasource.opds.model.OpdsPublication

interface OpdsDataSourceLocal: OpdsDataSource {

    suspend fun updateOpdsFeed(feed: DataLoadResult<OpdsFeed>)

    suspend fun updateOpdsPublication(publication: DataLoadResult<OpdsPublication>)

}