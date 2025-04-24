package world.respect.model
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import world.respect.validator.OpdsValidator

/**
 * JSON configuration for OPDS serialization/deserialization.
 * Uses lenient parsing with ignoreUnknownKeys to improve compatibility with different OPDS sources.
 */
object OpdsSerialization {
    private val module = SerializersModule {
        // Custom serializers can be added here if needed in the future
    }

    val json = Json {
        ignoreUnknownKeys = true        // Ignores unknown keys in JSON for compatibility
        isLenient = true                // Allows lenient parsing of the data
        prettyPrint = true              // Makes the output JSON human-readable
        serializersModule = module
    }

    /**
     * Parses an OPDS catalog JSON string into an OpdsCatalog object.
     *
     * @param jsonString The JSON string to parse
     * @return The parsed OpdsCatalog
     */
    fun parseOpdsCatalog(jsonString: String): OpdsCatalog {
        return json.decodeFromString<OpdsCatalog>(jsonString)
    }

    /**
     * Serializes an OpdsCatalog object to a JSON string.
     *
     * @param catalog The OpdsCatalog to serialize
     * @return The JSON string representation
     */
    fun serializeOpdsCatalog(catalog: OpdsCatalog): String {
        return json.encodeToString<OpdsCatalog>(catalog)
    }

    /**
     * Parses an OPDS publication JSON string into a Publication object.
     *
     * @param jsonString The JSON string to parse
     * @return The parsed Publication
     */
    fun parsePublication(jsonString: String): OpdsPublication {
        return try {
            json.decodeFromString<OpdsPublication>(jsonString)
        } catch (e: SerializationException) {
            throw OpdsValidator.ValidationException(listOf("Failed to deserialize publication: ${e.message}"))
        }
    }


    /**
     * Serializes a Publication object to a JSON string.
     *
     * @param publication The Publication to serialize
     * @return The JSON string representation
     */
    fun serializePublication(publication: OpdsPublication): String {
        return json.encodeToString<OpdsPublication>(publication)
    }
}