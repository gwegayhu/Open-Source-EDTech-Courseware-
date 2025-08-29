package world.respect.datalayer

import world.respect.datalayer.school.PersonDataSourceLocal

interface SchoolDataSourceLocal: SchoolDataSource {

    override val personDataSource: PersonDataSourceLocal

}