package world.respect.domain.opds.validator

import com.networknt.schema.ValidationMessage
import world.respect.domain.validator.ValidatorMessage

fun ValidationMessage.toValidatorMessage(sourceUri: String) = ValidatorMessage(
    isError = true,
    sourceUri = sourceUri,
    message = toString()
)
