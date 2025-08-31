package world.respect.datalayer.school

import world.respect.datalayer.school.model.Person

interface PersonDataSourceLocal: PersonDataSource {

    suspend fun putPersonsLocal(persons: List<Person>)

}