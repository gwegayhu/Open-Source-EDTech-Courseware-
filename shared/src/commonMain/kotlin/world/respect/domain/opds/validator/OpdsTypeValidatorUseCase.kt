package world.respect.domain.opds.validator

import world.respect.domain.validator.ValidatorUseCase
import world.respect.domain.validator.ValidatorReporter

interface OpdsTypeValidatorUseCase {

    suspend operator fun invoke(
        url: String,
        reporter: ValidatorReporter,
        visitedFeeds: MutableList<String>,
        linkValidator: ValidatorUseCase?,
    )

}