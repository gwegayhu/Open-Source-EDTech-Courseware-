package world.respect.datalayer

import world.respect.datalayer.realm.PersonDataSourceLocal

interface RespectRealmDataSourceLocal: RespectRealmDataSource {

    override val personDataSource: PersonDataSourceLocal

}