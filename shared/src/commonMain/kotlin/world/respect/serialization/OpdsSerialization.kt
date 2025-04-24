package world.respect.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import world.respect.model.OpdsCatalog
import world.respect.model.Publication


/**
 * JSON configuration for OPDS serialization/deserialization.
 * Uses lenient parsing with ignoreUnknownKeys to improve compatibility with different OPDS sources.
 */
object OpdsSerialization {
    private val module = SerializersModule {
        // custom serializers if needed in the future
    }

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
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
    fun parsePublication(jsonString: String): Publication {
        return json.decodeFromString<Publication>(jsonString)
    }

    /**
     * Serializes a Publication object to a JSON string.
     *
     * @param publication The Publication to serialize
     * @return The JSON string representation
     */
    fun serializePublication(publication: Publication): String {
        return json.encodeToString<Publication>(publication)
    }
}
