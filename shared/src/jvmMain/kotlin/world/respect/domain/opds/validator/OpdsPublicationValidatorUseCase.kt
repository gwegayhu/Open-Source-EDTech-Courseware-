package world.respect.domain.opds.validator

import com.networknt.schema.InputFormat
import world.respect.domain.validator.ValidatorUseCase
import world.respect.domain.validator.ValidatorMessage
import java.net.URI

class OpdsPublicationValidatorUseCase: AbstractOpdsTypeValidator(
    schemaUrl = "https://drafts.opds.io/schema/publication.schema.json"
) {

    override suspend fun invoke(
        url: String,
        visitedFeeds: MutableList<String>,
        linkValidator: ValidatorUseCase?
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