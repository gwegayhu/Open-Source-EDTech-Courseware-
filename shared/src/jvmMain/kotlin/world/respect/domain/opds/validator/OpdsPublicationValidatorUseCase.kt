package world.respect.domain.opds.validator

import com.networknt.schema.InputFormat
import kotlinx.serialization.json.Json
import world.respect.domain.validator.OpdsLinkValidatorUseCase
import world.respect.domain.validator.ValidatorMessage
import java.net.URI

class OpdsPublicationValidatorUseCase(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
): AbstractOpdsTypeValidator(
    schemaUrl = "https://drafts.opds.io/schema/publication.schema.json"
) {

    override fun invoke(
        url: String,
        visitedFeeds: MutableList<String>,
        linkValidator: OpdsLinkValidatorUseCase?
    ): List<ValidatorMessage> {
        val validationMessages = mutableListOf<ValidatorMessage>()

        try {
            val text = URI(url).toURL().readText()

            val messages = schema.validate(text, InputFormat.JSON)
            validationMessages.addAll(
                messages.map { it.toValidatorMessage(sourceUri = url) }
            )
        }catch (e: Throwable) {
            validationMessages += ValidatorMessage.fromException(url, e)
        }

        return validationMessages
    }
}