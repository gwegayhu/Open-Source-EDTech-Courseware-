package world.respect.datasource.opds

import world.respect.datasource.DataReadyState
import world.respect.datasource.opds.model.OpdsFeed
import world.respect.datasource.opds.model.OpdsPublication

interface OpdsDataSourceLocal: OpdsDataSource {

    suspend fun updateOpdsFeed(feed: DataReadyState<OpdsFeed>)

    suspend fun updateOpdsPublication(publication: DataReadyState<OpdsPublication>)

}