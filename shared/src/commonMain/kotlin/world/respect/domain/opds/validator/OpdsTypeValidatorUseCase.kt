package world.respect.domain.opds.validator

import world.respect.domain.validator.ValidatorUseCase
import world.respect.domain.validator.ValidatorMessage

interface OpdsTypeValidatorUseCase {

    suspend operator fun invoke(
        url: String,
        visitedFeeds: MutableList<String>,
        linkValidator: ValidatorUseCase?,
    ): List<ValidatorMessage>

}