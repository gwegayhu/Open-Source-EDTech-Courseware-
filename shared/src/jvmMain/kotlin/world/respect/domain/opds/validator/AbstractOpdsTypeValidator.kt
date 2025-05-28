package world.respect.domain.opds.validator

import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import world.respect.domain.validator.OpdsTypeValidatorUseCase
import java.net.URI

abstract class AbstractOpdsTypeValidator(
    private val schemaUrl: String,
) : OpdsTypeValidatorUseCase {

    private val factory by lazy {
        JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
    }

    protected val schema: JsonSchema by lazy {
        factory.getSchema(URI(schemaUrl)).also {
            it.initializeValidators()
        }
    }

}