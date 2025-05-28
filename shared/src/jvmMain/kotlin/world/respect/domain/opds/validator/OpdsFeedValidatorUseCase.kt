package world.respect.domain.opds.validator

import com.networknt.schema.InputFormat
import world.respect.domain.validator.ValidatorMessage
import kotlinx.serialization.json.Json
import world.respect.domain.opds.model.OpdsFeed
import world.respect.domain.validator.OpdsLinkValidatorUseCase
import java.net.URI

/**
 * Validate on OPDS Feed
 */
class OpdsFeedValidatorUseCase(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    },
) : AbstractOpdsTypeValidator(
    schemaUrl = "https://drafts.opds.io/schema/feed.schema.json"
) {

    override operator fun invoke(
        url: String,
        visitedFeeds: MutableList<String>,
        linkValidator: OpdsLinkValidatorUseCase?,
    ): List<ValidatorMessage> {
        val validationMessages = mutableListOf<ValidatorMessage>()

        try {
            val text = URI(url).toURL().readText()

            val messages = schema.validate(text, InputFormat.JSON)
            validationMessages.addAll(
                messages.map { it.toValidatorMessage(sourceUri = url) }
            )

            val opdsFeed = json.decodeFromString<OpdsFeed>(text)
            if(linkValidator != null) {
                val allLinks = opdsFeed.links + (opdsFeed.navigation ?: emptyList()) +
                        (opdsFeed.facets?.flatMap { it.links } ?: emptyList() ?: emptyList()) +
                        (opdsFeed.groups?.flatMap { it.links ?: emptyList() } ?: emptyList()) +
                        (opdsFeed.publications?.flatMap { it.links } ?: emptyList())


                allLinks.forEach { link ->
                    validationMessages += linkValidator(link, url, visitedFeeds, true)
                }
            }
        }catch(e : Throwable) {
            validationMessages += ValidatorMessage.fromException(url, e)
        }

        return validationMessages
    }


}