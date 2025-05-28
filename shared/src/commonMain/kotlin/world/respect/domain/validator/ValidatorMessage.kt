package world.respect.domain.validator

data class ValidatorMessage(
    val isError: Boolean,
    val sourceUri: String,
    val message: String,
) {

    companion object {

        fun fromException(
            sourceUri: String,
            throwable: Throwable
        ) = ValidatorMessage(
            isError = true,
            sourceUri = sourceUri,
            message = "Error processing $sourceUri : ${throwable.message}"
        )

    }

}
