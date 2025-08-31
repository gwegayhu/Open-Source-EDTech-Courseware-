package world.respect.datalayer.http.ext

import io.ktor.http.Url
import world.respect.datalayer.schooldirectory.SchoolDirectoryDataSource
import world.respect.libutil.ext.resolve

suspend fun SchoolDirectoryDataSource.resolveRespectExtUrlForSchool(
    schoolUrl: Url,
    href: String,
): Url {
    return getSchoolDirectoryEntryByUrl(schoolUrl)?.data?.respectExt?.resolve(href)
        ?: throw IllegalStateException("SchoolUrl $schoolUrl has no respect extensions URL")
}
