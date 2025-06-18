package world.respect.domain.opds.validator

import io.ktor.http.isSuccess
import world.respect.domain.respectappmanifest.validator.RespectAppManifestValidator
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.domain.validator.ValidateHttpResponseForUrlUseCase
import world.respect.domain.validator.ValidateLinkUseCase
import world.respect.domain.validator.ValidatorMessage
import world.respect.domain.validator.ValidatorReporter
import java.net.URI

class ValidateLinkUseCaseImpl(
    private val opdsFeedValidator: OpdsFeedValidator,
    private val opdsPublicationValidator: OpdsPublicationValidator,
    private val respectAppManifestValidator: RespectAppManifestValidator,
    private val validateHttpResponseForUrlUseCase: ValidateHttpResponseForUrlUseCase,
) : ValidateLinkUseCase {

    override suspend operator fun invoke(
        link: world.respect.datasource.opds.model.ReadiumLink,
        refererUrl: String,
        options: ValidateLinkUseCase.ValidatorOptions,
        reporter: ValidatorReporter,
        visitedUrls: MutableList<String>,
    ) {
        val linkType = link.type ?: world.respect.datasource.opds.model.OpdsFeed.MEDIA_TYPE

        val baseUrlUri = URI(refererUrl)
        val linkUrl = baseUrlUri.resolve(link.href).toURL().toString()

        if(linkUrl in visitedUrls) {
            reporter.addMessage(
                ValidatorMessage(
                    level = ValidatorMessage.Level.DEBUG,
                    sourceUri = refererUrl,
                    message = "Skipping link as it has already been visited: $linkUrl"
                )
            )
            return
        }

        visitedUrls.add(linkUrl)

        val httpResponseResult = validateHttpResponseForUrlUseCase(
            url = linkUrl,
            referer = refererUrl,
            reporter = reporter,
            options = if(options.skipRespectChecks) {
                ValidateHttpResponseForUrlUseCase.ONLY_CHECK_RESPONSE_IS_SUCCESS
            }else {
                ValidateHttpResponseForUrlUseCase.DEFAULT_VALIDATION_OPTS
            },
        )

        if(httpResponseResult.statusCode?.isSuccess() == false) {
            reporter.addMessage(
                ValidatorMessage(
                    level = ValidatorMessage.Level.DEBUG,
                    sourceUri = refererUrl,
                    message = "ValidateLinkUseCase: Skipping validator run for link to $linkUrl as " +
                            "it did not return a successful status code (${httpResponseResult.statusCode}): " +
                            linkUrl
                )
            )

            return
        }

        val validatorToRun = when(linkType) {
            world.respect.datasource.opds.model.OpdsFeed.MEDIA_TYPE -> {
                opdsFeedValidator
            }
            world.respect.datasource.opds.model.OpdsPublication.MEDIA_TYPE, world.respect.datasource.opds.model.OpdsPublication.MEDIA_TYPE_READIUM_MANIFEST -> {
                opdsPublicationValidator
            }
            RespectAppManifest.MIME_TYPE -> {
                respectAppManifestValidator
            }
            else -> null
        }

        validatorToRun?.invoke(
            url = linkUrl,
            options = options,
            reporter = reporter,
            visitedUrls = visitedUrls,
            linkValidator = if(options.followLinks) this else null,
        )
    }

}