package world.respect.domain.validator

interface OpdsTypeValidatorUseCase {

    operator fun invoke(
        url: String,
        visitedFeeds: MutableList<String>,
        linkValidator: OpdsLinkValidatorUseCase?,
    ): List<ValidatorMessage>

}