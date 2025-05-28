package world.respect.domain.opds.validator

import world.respect.domain.opds.model.OpdsFeed
import world.respect.domain.opds.model.ReadiumLink
import world.respect.domain.validator.OpdsLinkValidatorUseCase
import world.respect.domain.validator.ValidatorMessage
import java.net.URI

class OpdsLinkValidatorUseCaseImpl(
    private val opdsFeedValidatorUseCase: OpdsFeedValidatorUseCase,
    //..
) : OpdsLinkValidatorUseCase {

    override operator fun invoke(
        link: ReadiumLink,
        baseUrl: String,
        visitedUrls: MutableList<String>,
        followLinks: Boolean,
    ): List<ValidatorMessage> {
        val linkType = link.type ?: OpdsFeed.MEDIA_TYPE

        val baseUrlUri = URI(baseUrl)
        val linkUrl = baseUrlUri.resolve(link.href).toURL().toString()

        if(linkUrl in visitedUrls) {
            println("Already visited $linkUrl")
            return emptyList()
        }

        when(linkType) {
            OpdsFeed.MEDIA_TYPE -> {
                return opdsFeedValidatorUseCase(
                    url = link.href,
                    visitedFeeds = visitedUrls,
                    linkValidator = if(followLinks) this else null,
                )
            }

            else -> {
                return emptyList()
            }
        }
    }

}