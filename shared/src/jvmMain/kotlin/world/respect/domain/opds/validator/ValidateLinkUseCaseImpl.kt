package world.respect.domain.opds.validator

import world.respect.domain.opds.model.OpdsFeed
import world.respect.domain.opds.model.OpdsPublication
import world.respect.domain.opds.model.ReadiumLink
import world.respect.domain.validator.ValidateLinkUseCase
import world.respect.domain.validator.ValidatorReporter
import java.net.URI

class ValidateLinkUseCaseImpl(
    private val opdsFeedValidatorUseCase: ValidateOpdsFeedUseCase,
    private val opdsPublicationValidatorUseCase: ValidateOpdsPublicationUseCase,
) : ValidateLinkUseCase {

    override suspend operator fun invoke(
        link: ReadiumLink,
        baseUrl: String,
        reporter: ValidatorReporter,
        visitedUrls: MutableList<String>,
        followLinks: Boolean,
    ) {
        val linkType = link.type ?: OpdsFeed.MEDIA_TYPE

        val baseUrlUri = URI(baseUrl)
        val linkUrl = baseUrlUri.resolve(link.href).toURL().toString()

        if(linkUrl in visitedUrls) {
            println("Already visited $linkUrl")
            return
        }

        visitedUrls.add(linkUrl)

        when(linkType) {
            OpdsFeed.MEDIA_TYPE -> {
                opdsFeedValidatorUseCase(
                    url = linkUrl,
                    reporter = reporter,
                    visitedFeeds = visitedUrls,
                    linkValidator = if(followLinks) this else null,
                )
            }

            OpdsPublication.MEDIA_TYPE -> {
                opdsPublicationValidatorUseCase(
                    url = linkUrl,
                    reporter = reporter,
                    visitedFeeds = visitedUrls,
                    linkValidator = if(followLinks) this else null,
                )
            }
        }
    }

}