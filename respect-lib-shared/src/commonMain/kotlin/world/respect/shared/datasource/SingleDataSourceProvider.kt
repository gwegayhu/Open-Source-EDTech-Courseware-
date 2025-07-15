package world.respect.shared.datasource

import world.respect.shared.domain.account.RespectAccount
import world.respect.datalayer.RespectAppDataSource

class SingleDataSourceProvider(
    private val datasource: RespectAppDataSource
): RespectAppDataSourceProvider {

    override fun getDataSource(account: RespectAccount): RespectAppDataSource {
        return datasource
    }
}