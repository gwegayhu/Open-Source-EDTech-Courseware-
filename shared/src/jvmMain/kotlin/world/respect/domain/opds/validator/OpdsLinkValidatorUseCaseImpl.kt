package world.respect.domain.opds.validator

import world.respect.domain.opds.model.OpdsFeed
import world.respect.domain.opds.model.OpdsPublication
import world.respect.domain.opds.model.ReadiumLink
import world.respect.domain.validator.ValidatorUseCase
import world.respect.domain.validator.ValidatorMessage
import java.net.URI

class OpdsLinkValidatorUseCaseImpl(
    private val opdsFeedValidatorUseCase: OpdsFeedValidatorUseCase,
    private val opdsPublicationValidatorUseCase: OpdsPublicationValidatorUseCase,
) : ValidatorUseCase {

    override suspend operator fun invoke(
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

        visitedUrls.add(linkUrl)

        return when(linkType) {
            OpdsFeed.MEDIA_TYPE -> {
                opdsFeedValidatorUseCase(
                    url = linkUrl,
                    visitedFeeds = visitedUrls,
                    linkValidator = if(followLinks) this else null,
                )
            }

            OpdsPublication.MEDIA_TYPE -> {
                opdsPublicationValidatorUseCase(
                    url = linkUrl,
                    visitedFeeds = visitedUrls,
                    linkValidator = if(followLinks) this else null,
                )
            }

            else -> {
                emptyList()
            }
        }
    }

}