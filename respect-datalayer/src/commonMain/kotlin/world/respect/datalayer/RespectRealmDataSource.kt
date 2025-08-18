package world.respect.datalayer

import world.respect.datalayer.realm.PersonDataSource

interface RespectRealmDataSource {

    val personDataSource: PersonDataSource

}