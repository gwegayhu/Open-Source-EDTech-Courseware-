package world.respect.datalayer.opds

import world.respect.datalayer.DataReadyState
import world.respect.datalayer.opds.model.OpdsFeed
import world.respect.datalayer.opds.model.OpdsPublication

interface OpdsDataSourceLocal: OpdsDataSource {

    suspend fun updateOpdsFeed(feed: DataReadyState<OpdsFeed>)

    suspend fun updateOpdsPublication(publication: DataReadyState<OpdsPublication>)

}