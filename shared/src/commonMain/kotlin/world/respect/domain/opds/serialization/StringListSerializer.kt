package world.respect.domain.opds.serialization

import kotlinx.serialization.builtins.serializer

object StringListSerializer: SingleItemToListTransformer<String>(String.serializer())
