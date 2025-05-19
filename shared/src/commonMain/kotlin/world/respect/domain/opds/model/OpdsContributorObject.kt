package world.respect.domain.opds.model

import kotlinx.serialization.Serializable
import world.respect.domain.opds.serialization.SingleItemToListTransformer
import world.respect.domain.opds.serialization.StringOrObjectSerializer
import world.respect.domain.opds.serialization.StringValue
import world.respect.domain.opds.serialization.StringValueSerializer

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
data class OpdsContributorStringValue(override val value: String): OpdsContributor(), StringValue

object OpdsContributorStringValueSerializer : StringValueSerializer<OpdsContributorStringValue>(
    serialName = "respect.world.OpdsContributorStringValue",
    stringToValue = { OpdsContributorStringValue(it) }
)

object OpdsContributorSerializer: StringOrObjectSerializer<OpdsContributor>(
    OpdsContributor::class,
    primitiveSerializer = OpdsContributorStringValue.serializer(),
    objectSerializer = OpdsContributorObject.serializer()
)

object OpdsSingleItemToListTransformer: SingleItemToListTransformer<OpdsContributor>(OpdsContributor.serializer())
