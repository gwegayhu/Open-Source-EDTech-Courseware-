package world.respect.datalayer.repository

import world.respect.datalayer.RespectRealmDataSource
import world.respect.datalayer.RespectRealmDataSourceLocal
import world.respect.datalayer.oneroster.rostering.OneRosterDataSource
import world.respect.datalayer.realm.PersonDataSource

class RespectRealmDataSourceRepository(
    private val local: RespectRealmDataSourceLocal,
    private val remote: RespectRealmDataSource,
) : RespectRealmDataSource {

    override val personDataSource: PersonDataSource
        get() = TODO("Not yet implemented")

    override val onRoasterDataSource: OneRosterDataSource
        get() = TODO("Not yet implemented")
}