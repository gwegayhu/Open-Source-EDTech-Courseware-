package world.respect.serialization

import kotlinx.serialization.json.Json
import world.respect.model.RespectManifest

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
