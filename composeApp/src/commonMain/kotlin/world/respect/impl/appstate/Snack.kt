package world.respect.impl.appstate

data class Snack(
    val message: String,
    val action: String? = null,
    val onAction: (() -> Unit)? = null,
)
