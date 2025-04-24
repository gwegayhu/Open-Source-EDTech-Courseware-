package world.respect.model

import kotlinx.serialization.Serializable

/**
 * Represents the RESPECT manifest as described in the RESPECT Launcher App Integration Guide.
 * This manifest provides information about the app for the RESPECT launcher.
 */
@Serializable
data class RespectManifest(
    val name: Map<String, String>,
    val license: String,
    val learningUnits: String,
    val defaultLaunchUri: String,
    val android: AndroidDetails? = null
) {
    companion object {
        const val DEFAULT_MANIFEST_PATH = ".well-known/respect-app.json"
    }
}

/**
 * Contains Android-specific details for the RESPECT manifest.
 */
@Serializable
data class AndroidDetails(
    val packageId: String,
    val stores: List<String> = emptyList()
)