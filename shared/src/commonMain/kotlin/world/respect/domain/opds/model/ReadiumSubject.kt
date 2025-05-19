package world.respect.domain.opds.model

import com.eygraber.uri.Uri
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import world.respect.domain.opds.serialization.SingleItemToListTransformer
import world.respect.domain.opds.serialization.StringOrObjectSerializer

/**
 * Schema: https://readium.org/webpub-manifest/schema/subject.schema.json
 */
@Serializable(with = ReadiumSubjectSerializer::class)
sealed class ReadiumSubject

@Serializable(with = ReadiumSubjectStringValueSerializer::class)
data class ReadiumSubjectStringValue(val value: String): ReadiumSubject()

object ReadiumSubjectStringValueSerializer: KSerializer<ReadiumSubjectStringValue> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "world.respet.ReadiumSubjectStringValue", PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder) = ReadiumSubjectStringValue(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ReadiumSubjectStringValue) = encoder.encodeString(value.value)
}

/**
 * Schema: https://readium.org/webpub-manifest/schema/subject-object.schema.json
 */
@Serializable
data class ReadiumSubjectObject(
    //TODO: Name should be string or langmap
    val name: String,
    val sortAs: String? = null,
    val code: String? = null,
    val scheme: Uri? = null,
    val links: List<OpdsLink>? = null,
): ReadiumSubject()

object ReadiumSubjectSerializer: StringOrObjectSerializer<ReadiumSubject>(
    ReadiumSubject::class,
    primitiveSerializer = ReadiumSubjectStringValue.serializer(),
    objectSerializer = ReadiumSubjectObject.serializer()
)

object ReadiumSubjectToListTransformer: SingleItemToListTransformer<ReadiumSubject>(
    ReadiumSubject.serializer()
)
