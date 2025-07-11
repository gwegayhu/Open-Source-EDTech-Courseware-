package world.respect.datalayer.opds.model

import com.eygraber.uri.Uri
import kotlinx.serialization.Serializable
import world.respect.datalayer.opds.serialization.SingleItemToListTransformer
import world.respect.datalayer.opds.serialization.StringOrObjectSerializer
import world.respect.datalayer.opds.serialization.StringValue
import world.respect.datalayer.opds.serialization.StringValueSerializer
import world.respect.datalayer.opds.serialization.UriStringSerializer

/**
 * Schema: https://readium.org/webpub-manifest/schema/subject.schema.json
 */
@Serializable(with = ReadiumSubjectSerializer::class)
sealed class ReadiumSubject

@Serializable(with = ReadiumSubjectStringValueSerializer::class)
data class ReadiumSubjectStringValue(override val value: String): ReadiumSubject(), StringValue

object ReadiumSubjectStringValueSerializer: StringValueSerializer<ReadiumSubjectStringValue>(
    serialName = "ReadiumSubjectStringValue", stringToValue = {
        ReadiumSubjectStringValue(
            it
        )
    }
)

/**
 * Schema: https://readium.org/webpub-manifest/schema/subject-object.schema.json
 */
@Serializable
data class ReadiumSubjectObject(
    val name: LangMap,
    val sortAs: String? = null,
    val code: String? = null,
    @Serializable(with = UriStringSerializer::class)
    val scheme: Uri? = null,
    val links: List<ReadiumLink>? = null,
): ReadiumSubject()

object ReadiumSubjectSerializer: StringOrObjectSerializer<ReadiumSubject>(
    ReadiumSubject::class,
    primitiveSerializer = ReadiumSubjectStringValue.serializer(),
    objectSerializer = ReadiumSubjectObject.serializer()
)

object ReadiumSubjectToListTransformer: SingleItemToListTransformer<ReadiumSubject>(
    ReadiumSubject.serializer()
)
