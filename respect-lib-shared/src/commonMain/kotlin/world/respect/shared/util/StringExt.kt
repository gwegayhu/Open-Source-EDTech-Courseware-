package world.respect.shared.util


fun String.requirePostfix(
    postFix: String,
    ignoreCase: Boolean = false
) = if(this.endsWith(postFix, ignoreCase)) this else "$this$postFix"
