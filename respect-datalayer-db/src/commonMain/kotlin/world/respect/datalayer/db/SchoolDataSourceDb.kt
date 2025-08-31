package world.respect.datalayer.db

import world.respect.datalayer.AuthenticatedUserPrincipalId
import world.respect.datalayer.SchoolDataSourceLocal
import world.respect.datalayer.db.school.PersonDataSourceDb
import world.respect.datalayer.school.PersonDataSourceLocal
import world.respect.libxxhash.XXStringHasher

class SchoolDataSourceDb(
    private val schoolDb: RespectSchoolDatabase,
    private val xxStringHasher: XXStringHasher,
    private val authenticatedUser: AuthenticatedUserPrincipalId,
) : SchoolDataSourceLocal{

    override val personDataSource: PersonDataSourceLocal by lazy {
        PersonDataSourceDb(schoolDb, xxStringHasher)
    }

}