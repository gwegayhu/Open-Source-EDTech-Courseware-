package world.respect.datalayer.realm

import world.respect.datalayer.realm.model.Person

interface PersonDataSource {

    suspend fun findByUsername(username: String): Person?

}