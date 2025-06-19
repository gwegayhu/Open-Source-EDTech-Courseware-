package world.respect.datasource.opds.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataResult
import world.respect.datasource.opds.OpdsDataSource

class FakeOpdsDataSource : OpdsDataSource {

    override fun loadOpdsFeed(
        url: String,
        params: DataLoadParams
    ): Flow<DataResult<OpdsFeed>> = flow {
        emit(
            DataLoadResult(
                data = OpdsFeed(
                    metadata = TODO(),
                    links = TODO(),
                    publications = listOf(
                        OpdsPublication(
                            metadata = ReadiumMetadata(
                                title = LangMapStringValue("Lesson 001"),
                                author = listOf(ReadiumContributorStringValue("Mullah Nasruddin")),
                                language = listOf("en"),
                                modified = "2015-09-29T17:00:00Z",
                                subject = listOf(ReadiumSubjectStringValue("Urdu")),
                                duration = 2.0
                            ),
                            links = listOf(
                                ReadiumLink(
                                    href = "",
                                    type = "application/opds-publication+json",
                                    rel = listOf("self")
                                ),
                                ReadiumLink(
                                    href = "",
                                    type = "text/html",
                                    rel = listOf("http://opds-spec.org/acquisition/open-access")
                                )
                            ),
                            images = listOf(
                                ReadiumLink(
                                    href = "",
                                    type = "image/jpeg",
                                    height = 700,
                                    width = 400
                                )
                            )
                        )
                    ),
                    navigation = TODO(),
                    facets = listOf(
                        OpdsFacet(
                            metadata = OpdsFeedMetadata(
                                title = "Language",
                                identifier = null,
                                type = null,
                                subtitle = null,
                                modified = null,
                                description = null,
                                itemsPerPage = null,
                                currentPage = null,
                                numberOfItems = null,
                            ),
                            links = listOf(
                                ReadiumLink(
                                    href = "/fr",
                                    type = "application/opds+json",
                                    title = "French"
                                ),
                                ReadiumLink(
                                    href = "/en",
                                    type = "application/opds+json",
                                    title = "English"
                                )
                            )
                        )
                    ),
                    groups = TODO()
                ),
                status = TODO(),
            )
        )



    }

    override fun loadOpdsPublication(
        url: String,
        params: DataLoadParams,
        referrerUrl: String?,
        expectedPublicationId: String?
    ): Flow<DataResult<OpdsPublication>> {
        TODO("Not required for lesson list at the moment")
    }
}