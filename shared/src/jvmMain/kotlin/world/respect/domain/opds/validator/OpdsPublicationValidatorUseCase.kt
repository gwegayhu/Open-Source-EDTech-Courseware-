package world.respect.domain.opds.validator

import world.respect.domain.validator.OpdsTypeValidatorUseCase
import world.respect.domain.validator.OpdsLinkValidatorUseCase
import world.respect.domain.validator.ValidatorMessage

class OpdsPublicationValidatorUseCase: OpdsTypeValidatorUseCase {

    override fun invoke(
        url: String,
        visitedFeeds: MutableList<String>,
        linkValidator: OpdsLinkValidatorUseCase?
    ): List<ValidatorMessage> {
        TODO()
    }
}