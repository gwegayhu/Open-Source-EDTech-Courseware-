package world.respect.datalayer.opds

import world.respect.datalayer.DataReadyState
import world.respect.datalayer.networkvalidation.NetworkDataSourceValidationHelper
import world.respect.datalayer.opds.model.OpdsFeed
import world.respect.datalayer.opds.model.OpdsPublication

interface OpdsDataSourceLocal: OpdsDataSource {

    val feedNetworkValidationHelper: NetworkDataSourceValidationHelper

    val publicationNetworkValidationHelper: NetworkDataSourceValidationHelper

    suspend fun updateOpdsFeed(feed: DataReadyState<OpdsFeed>)

    suspend fun updateOpdsPublication(publication: DataReadyState<OpdsPublication>)

}