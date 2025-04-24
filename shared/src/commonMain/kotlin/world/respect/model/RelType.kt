package world.respect.model


/**
 * Represents the rel field which can be either a single string or an array of strings.
 * The rel field defines the relationship between a resource and its linked resource.
 *
 * For reference, see the schema: https://readium.org/webpub-manifest/schema/link.schema.json
 */
sealed class RelType {

    /**
     * Checks if this relation contains the specified value.
     */
    abstract fun contains(value: String): Boolean

    /**
     * A single relation value.
     */
    data class Single(val value: String) : RelType() {
        override fun contains(value: String): Boolean = this.value == value
    }

    /**
     * Multiple relation values.
     */
    data class Multiple(val values: List<String>) : RelType() {
        override fun contains(value: String): Boolean = values.contains(value)
    }

    companion object {
        /**
         * Creates a RelType from a string or string array.
         */
        fun from(value: Any): RelType {
            return when (value) {
                is String -> Single(value)
                is List<*> -> Multiple(value.filterIsInstance<String>())
                else -> throw IllegalArgumentException("Unsupported type for rel: ${value::class}")
            }
        }
    }
}

