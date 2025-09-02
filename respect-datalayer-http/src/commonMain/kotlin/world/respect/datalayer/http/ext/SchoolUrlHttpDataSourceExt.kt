package world.respect.datalayer.http.ext

import io.ktor.http.Url
import world.respect.datalayer.ext.dataOrNull
import world.respect.datalayer.http.school.SchoolUrlBasedDataSource
import world.respect.libutil.ext.appendEndpointSegments

suspend fun SchoolUrlBasedDataSource.respectEndpointUrl(path: String): Url {
    return schoolDirectoryDataSource.getSchoolDirectoryEntryByUrl(schoolUrl).dataOrNull()
        ?.respectExt?.appendEndpointSegments(path)
        ?: throw IllegalArgumentException("SchoolUrl $schoolUrl has no respect extensions URL")
}