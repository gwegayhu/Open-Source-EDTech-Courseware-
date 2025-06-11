package world.respect.domain.validator

class ListAndPrintlnValidatorReporter: ValidatorReporter {

    private val _messages = mutableListOf<ValidatorMessage>()

    val messages: List<ValidatorMessage>
        get() = _messages.toList()

    override fun addMessage(message: ValidatorMessage) : ValidatorMessage{
        _messages.add(message)
        println(
            buildString {
                append(if(message.isError) "ERROR: " else "WARNING: ")
                append(message.sourceUri)
                append(" ")
                append(message.message)
                append("\n")
            }
        )
        return message
    }
}