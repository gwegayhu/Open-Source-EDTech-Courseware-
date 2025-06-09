package world.respect.domain.opds.validator

import com.networknt.schema.InputFormat
import world.respect.domain.validator.ValidatorMessage
import kotlinx.serialization.json.Json
import world.respect.domain.opds.model.OpdsFeed
import world.respect.domain.validator.ValidatorReporter
import world.respect.domain.validator.ValidateLinkUseCase
import java.net.URI

/**
 * Validate on OPDS Feed
 */
class ValidateOpdsFeedUseCase(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    },
) : AbstractOpdsTypeValidator(
    schemaUrl = "https://drafts.opds.io/schema/feed.schema.json"
) {

    override suspend operator fun invoke(
        url: String,
        options: ValidateLinkUseCase.ValidatorOptions,
        reporter: ValidatorReporter,
        visitedFeeds: MutableList<String>,
        linkValidator: ValidateLinkUseCase?,
    ) {
        try {
            val text = URI(url).toURL().readText()

            val messages = schema.validate(text, InputFormat.JSON)
            messages.forEach {
                reporter.addMessage(it.toValidatorMessage(sourceUri = url))
            }

            val opdsFeed = json.decodeFromString<OpdsFeed>(text)
            if(linkValidator != null) {
                val allLinks = opdsFeed.links + (opdsFeed.navigation ?: emptyList()) +
                        (opdsFeed.facets?.flatMap { it.links } ?: emptyList() ?: emptyList()) +
                        (opdsFeed.groups?.flatMap { it.links ?: emptyList() } ?: emptyList()) +
                        (opdsFeed.publications?.flatMap { it.links } ?: emptyList())


                allLinks.forEach { link ->
                    linkValidator(link, url, options, reporter, visitedFeeds)
                }
            }
        }catch(e : Throwable) {
            reporter.addMessage(ValidatorMessage.fromException(url, e))
        }
    }


}