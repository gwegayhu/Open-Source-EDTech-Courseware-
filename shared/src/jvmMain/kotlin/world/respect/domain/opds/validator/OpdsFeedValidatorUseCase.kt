package world.respect.domain.opds.validator

import com.networknt.schema.InputFormat
import world.respect.domain.validator.ValidatorMessage
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import kotlinx.serialization.json.Json
import world.respect.domain.opds.model.OpdsFeed
import world.respect.domain.validator.OpdsLinkValidatorUseCase
import world.respect.domain.validator.OpdsTypeValidatorUseCase
import java.net.URI

/**
 * Validate on OPDS Feed
 */
class OpdsFeedValidatorUseCase(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    },
) : OpdsTypeValidatorUseCase{

    private val factory by lazy {
        JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
    }

    private val feedSchema by lazy {
        factory.getSchema(URI("https://drafts.opds.io/schema/feed.schema.json")).also {
            it.initializeValidators()
        }
    }

    override operator fun invoke(
        url: String,
        visitedFeeds: MutableList<String>,
        linkValidator: OpdsLinkValidatorUseCase?,
    ): List<ValidatorMessage> {
        val validationMessages = mutableListOf<ValidatorMessage>()

        try {
            println("Loading $url ...")

            val urlUri = URI(url)
            val text = urlUri.toURL().readText()
            visitedFeeds.add(url)

            val messages = feedSchema.validate(text, InputFormat.JSON)
            validationMessages.addAll(
                messages.map {
                    ValidatorMessage(
                        isError = true,
                        sourceUri = url,
                        message = it.toString()
                    )
                }
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
            validationMessages += ValidatorMessage(
                isError = true,
                sourceUri = url,
                message = "Error processing $url : ${e.message}"
            )
        }

        return validationMessages
    }


}