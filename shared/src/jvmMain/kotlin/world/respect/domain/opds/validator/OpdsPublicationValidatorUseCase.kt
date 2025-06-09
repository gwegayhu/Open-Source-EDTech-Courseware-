package world.respect.domain.opds.validator

import com.networknt.schema.InputFormat
import world.respect.domain.validator.ValidatorUseCase
import world.respect.domain.validator.ValidatorMessage
import world.respect.domain.validator.ValidatorReporter
import java.net.URI

class OpdsPublicationValidatorUseCase: AbstractOpdsTypeValidator(
    schemaUrl = "https://drafts.opds.io/schema/publication.schema.json"
) {

    override suspend fun invoke(
        url: String,
        reporter: ValidatorReporter,
        visitedFeeds: MutableList<String>,
        linkValidator: ValidatorUseCase?
    ) {
        try {
            val text = URI(url).toURL().readText()

            val messages = schema.validate(text, InputFormat.JSON)
            messages.forEach {
                reporter.addMessage(it.toValidatorMessage(sourceUri = url))
            }
        }catch (e: Throwable) {
            reporter.addMessage(ValidatorMessage.fromException(url, e))
        }
    }
}