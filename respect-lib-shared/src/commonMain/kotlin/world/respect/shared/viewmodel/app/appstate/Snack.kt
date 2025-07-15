package world.respect.shared.viewmodel.app.appstate

data class Snack(
    val message: String,
    val action: String? = null,
    val onAction: (() -> Unit)? = null,
)
