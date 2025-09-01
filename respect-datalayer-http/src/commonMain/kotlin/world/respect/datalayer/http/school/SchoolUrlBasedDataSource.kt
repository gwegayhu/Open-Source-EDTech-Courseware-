package world.respect.datalayer.http.school

import io.ktor.http.Url
import world.respect.datalayer.schooldirectory.SchoolDirectoryDataSource

/**
 * Interface for http SchoolDataSources . Getting the URL for a particular endpoint (e.g. Respect
 * extensions, xAPI, etc) is itself asynchronous because this requires loading the
 * SchoolDirectoryEntry from the database and/or network.
 */
interface SchoolUrlBasedDataSource {

    val schoolUrl: Url

    val schoolDirectoryDataSource: SchoolDirectoryDataSource

}