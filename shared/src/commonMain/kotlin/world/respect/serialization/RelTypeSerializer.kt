package world.respect.serialization


import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import world.respect.model.RelType

/**
 * Custom serializer for RelType to handle both string and array formats.
 * This allows the rel field to be deserialized from either a string or an array of strings.
 */
object RelTypeSerializer : KSerializer<RelType> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RelType")

    override fun serialize(encoder: Encoder, value: RelType) {
        require(encoder is JsonEncoder) { "This serializer can only be used with JSON" }
        val jsonElement = when (value) {
            is RelType.Single -> JsonPrimitive(value.value)
            is RelType.Multiple -> JsonArray(value.values.map { JsonPrimitive(it) })
        }
        encoder.encodeJsonElement(jsonElement)
    }

    override fun deserialize(decoder: Decoder): RelType {
        require(decoder is JsonDecoder) { "This serializer can only be used with JSON" }

        return when (val element = decoder.decodeJsonElement()) {
            is JsonPrimitive -> {
                if (!element.isString) {
                    throw SerializationException("Expected a string for rel")
                }
                RelType.Single(element.content)
            }
            is JsonArray -> {
                val values = element.mapNotNull {
                    (it as? JsonPrimitive)?.takeIf { it.isString }?.content
                }
                RelType.Multiple(values)
            }
            else -> throw SerializationException("Expected a string or array for rel")
        }
    }
}