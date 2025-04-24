package world.respect.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

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

/**
 * JSON configuration for RESPECT manifest serialization/deserialization.
 */
object RespectSerialization {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
    }

    /**
     * Parses a RESPECT manifest JSON string into a RespectManifest object.
     *
     * @param jsonString The JSON string to parse
     * @return The parsed RespectManifest
     */
    fun parseManifest(jsonString: String): RespectManifest {
        return json.decodeFromString<RespectManifest>(jsonString)
    }

    /**
     * Serializes a RespectManifest object to a JSON string.
     *
     * @param manifest The RespectManifest to serialize
     * @return The JSON string representation
     */
    fun serializeManifest(manifest: RespectManifest): String {
        return json.encodeToString<RespectManifest>(manifest)
    }
}
