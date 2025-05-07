package world.respect.app.appstate

/**
 * Represents the Floating Action Button.
 */
data class FabUiState(
    val visible: Boolean = true,
    val text: String? = "Add",
    val icon: FabIcon = FabIcon.ADD,
    val onClick: () -> Unit = { },
) {

    enum class FabIcon {
        NONE, ADD, EDIT
    }

}