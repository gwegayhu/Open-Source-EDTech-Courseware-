package world.respect.datalayer.http.ext

import io.ktor.http.Url
import world.respect.datalayer.ext.dataOrNull
import world.respect.datalayer.http.school.SchoolUrlBasedDataSource
import world.respect.libutil.ext.resolve

suspend fun SchoolUrlBasedDataSource.resolveRespectExtUrl(path: String): Url {
    return schoolDirectoryDataSource.getSchoolDirectoryEntryByUrl(schoolUrl).dataOrNull()
        ?.respectExt?.resolve(path)
        ?: throw IllegalStateException("SchoolUrl $schoolUrl has no respect extensions URL")
}