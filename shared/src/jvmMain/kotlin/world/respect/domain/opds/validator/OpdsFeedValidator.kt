package world.respect.domain.opds.validator

import com.networknt.schema.InputFormat
import io.ktor.client.HttpClient
import world.respect.domain.validator.ValidatorMessage
import kotlinx.serialization.json.Json
import world.respect.domain.opds.model.OpdsFeed
import world.respect.domain.opds.model.OpdsPublication
import world.respect.domain.opds.model.ReadiumLink
import world.respect.domain.validator.ValidatorReporter
import world.respect.domain.validator.ValidateLinkUseCase

/**
 * Validate on OPDS Feed
 */
class OpdsFeedValidator(
    private val json: Json,
    private val httpClient: HttpClient,
    private val validateOpdsPublicationUseCase: ValidateOpdsPublicationUseCase,
) : AbstractJsonSchemaValidator(
    schemaUrl = "https://drafts.opds.io/schema/feed.schema.json"
) {

    override suspend operator fun invoke(
        url: String,
        options: ValidateLinkUseCase.ValidatorOptions,
        reporter: ValidatorReporter,
        visitedUrls: MutableList<String>,
        linkValidator: ValidateLinkUseCase?,
    ) {
        val discoveredManifests = mutableListOf<ReadiumLink>()
        try {
            val text = httpClient.verifyMimeTypeAndGetBodyAsText(
                url = url,
                acceptableMimeTypes = listOf(OpdsPublication.MEDIA_TYPE, "application/json"),
                reporter = reporter
            )

            val messages = schema.validate(text, InputFormat.JSON)
            messages.forEach {
                reporter.addMessage(it.toValidatorMessage(sourceUri = url))
            }

            val opdsFeed = json.decodeFromString<OpdsFeed>(text)

            val allPublications = (opdsFeed.publications ?: emptyList()) +
                    (opdsFeed.groups?.flatMap { it.publications ?: emptyList() } ?: emptyList())

            allPublications.forEach {
                val publicationValidation = validateOpdsPublicationUseCase(it, url, reporter)
                discoveredManifests.addAll(publicationValidation.discoveredManifestLinksToValidate)
            }

            if(linkValidator != null) {
                val allLinks = opdsFeed.links + (opdsFeed.navigation ?: emptyList()) +
                        (opdsFeed.facets?.flatMap { it.links } ?: emptyList() ?: emptyList()) +
                        (opdsFeed.groups?.flatMap { it.links ?: emptyList() } ?: emptyList()) +
                        (opdsFeed.publications?.flatMap { it.links } ?: emptyList())

                (allLinks + discoveredManifests).forEach { link ->
                    linkValidator(link, url, options, reporter, visitedUrls)
                }
            }
        }catch(e : Throwable) {
            reporter.addMessage(ValidatorMessage.fromException(url, e))
        }
    }


}