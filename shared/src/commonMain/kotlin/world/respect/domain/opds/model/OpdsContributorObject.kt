package world.respect.domain.opds.model

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import world.respect.domain.opds.serialization.SingleItemToListTransformer

/**
 * Represents a contributor (author, translator, etc.). As per the schema, this can be an object,
 * single string value, list of strings, or list of contributor objects. Fields of the
 * OpdsContributor type should use a List Kotlin type and use the OpdsSingleItemToListTransformer
 * serializer to convert single items into a single item list.
 *
 * For reference, see the schema:
 * https://github.com/readium/webpub-manifest/blob/master/schema/contributor.schema.json
 */
@Serializable(with = OpdsContributorSerializer::class)
sealed class OpdsContributor

@Serializable
data class OpdsContributorObject(
    val name: String,
    val identifier: String? = null,
    val sortAs: String? = null,
    val links: List<OpdsLink>? = null,
): OpdsContributor()

@Serializable(with = OpdsContributorStringValueSerializer::class)
data class OpdsContributorStringValue(val value: String): OpdsContributor()

object OpdsContributorStringValueSerializer : KSerializer<OpdsContributorStringValue> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "world.respect.OpdsContributorStringValue", PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder) = OpdsContributorStringValue(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: OpdsContributorStringValue) = encoder.encodeString(value.value)
}

object OpdsContributorSerializer: JsonContentPolymorphicSerializer<OpdsContributor>(OpdsContributor::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<OpdsContributor> {
        return when(element) {
            is JsonPrimitive -> OpdsContributorStringValue.serializer()
            else -> OpdsContributorObject.serializer()
        }
    }
}

object OpdsSingleItemToListTransformer: SingleItemToListTransformer<OpdsContributor>(OpdsContributor.serializer())
