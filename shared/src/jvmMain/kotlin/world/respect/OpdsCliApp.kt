package world.respect

import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject
import world.respect.domain.opds.model.OpdsFeed
import world.respect.domain.opds.model.OpdsPublication
import world.respect.domain.opds.validator.OpdsValidator
import java.io.File
import java.nio.file.Paths
import kotlin.jvm.JvmStatic

/**
 * Command line application for validating OPDS files.
 * This application automatically detects whether a file is an OPDS feed or publication
 * and validates it according to the OPDS 2.0 specification.
 */
object OpdsCliApp {

    // Json instance for serialization and deserialization
    private val json = Json {
        ignoreUnknownKeys = true  // Ignores unknown keys in JSON for compatibility
        isLenient = true          // Allows lenient parsing of the data
        prettyPrint = true        // Makes the output JSON human-readable
    }

    /**
     * Prints help information for the application.
     */
    private fun printHelp() {
        println("OPDS Validator - Command Line Application")
        println("Usage: java -jar opds-validator.jar <file>")
        println("\nExample:")
        println("  java -jar opds-validator.jar catalog.json")
        println("\nThis tool validates OPDS 2.0 files used for listing learning units in RESPECT.")
    }

    /**
     * Main entry point for the command line application.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty() || args[0] == "--help" || args[0] == "-h") {
            printHelp()
            return
        }

        val filePath = args[0]

        try {
            val absolutePath = Paths.get(filePath).toAbsolutePath().toString()
            validateOpdsFile(absolutePath)
        } catch (e: Exception) {
            println("Error with file path: ${e.message}")
        }
    }

    /**
     * Validates an OPDS file, determining if it's a valid feed or publication.
     *
     * @param filePath The path to the OPDS file
     */
    private fun validateOpdsFile(filePath: String) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                println("Error: File not found: $filePath")
                return
            }

            val jsonContent = file.readText()

            // First, check if it's valid JSON and analyze to determine type
            try {
                val jsonElement = json.parseToJsonElement(jsonContent)
                if (jsonElement !is JsonObject) {
                    println("Error: Not a valid JSON object")
                    return
                }

                // Determine if this is more likely a feed or publication
                val isProbablyFeed = isProbablyFeed(jsonElement)
                val validator = OpdsValidator()

                // First try the most likely format based on structure analysis
                if (isProbablyFeed) {
                    try {
                        val feed = json.decodeFromString<OpdsFeed>(jsonContent)
                        val result = validator.validateFeed(feed)

                        if (result.isSuccess) {
                            println("Validation successful: The file is a valid OPDS feed.")
                            return
                        } else {
                            // Only try as publication if feed validation failed
                            val exception = result.exceptionOrNull() as? OpdsValidator.ValidationException
                            // Don't show feed errors yet, try publication first

                            try {
                                val publication = json.decodeFromString<OpdsPublication>(jsonContent)
                                val pubResult = validator.validatePublication(publication)

                                if (pubResult.isSuccess) {
                                    println("Validation successful: The file is a valid OPDS publication.")
                                    return
                                } else {
                                    // Both failed, show feed errors since that was more likely
                                    println("Validation failed: The file is not a valid OPDS feed.")
                                    println("Errors:")
                                    exception?.errors?.forEach { println("- $it") }
                                }
                            } catch (e: SerializationException) {
                                // Only feed validation was possible, show those errors
                                println("Validation failed: The file is not a valid OPDS feed.")
                                println("Errors:")
                                exception?.errors?.forEach { println("- $it") }
                            }
                        }
                    } catch (e: SerializationException) {
                        // Try as publication instead
                        try {
                            val publication = json.decodeFromString<OpdsPublication>(jsonContent)
                            val result = validator.validatePublication(publication)

                            if (result.isSuccess) {
                                println("Validation successful: The file is a valid OPDS publication.")
                            } else {
                                val exception = result.exceptionOrNull() as? OpdsValidator.ValidationException
                                println("Validation failed: The file is not a valid OPDS publication.")
                                println("Errors:")
                                exception?.errors?.forEach { println("- $it") }
                            }
                        } catch (e2: SerializationException) {
                            println("Error: The file is not a valid OPDS feed or publication.")
                            println("Details: ${e2.message}")
                        }
                    }
                } else {
                    // Try as publication first
                    try {
                        val publication = json.decodeFromString<OpdsPublication>(jsonContent)
                        val result = validator.validatePublication(publication)

                        if (result.isSuccess) {
                            println("Validation successful: The file is a valid OPDS publication.")
                            return
                        } else {
                            // Only try as feed if publication validation failed
                            val exception = result.exceptionOrNull() as? OpdsValidator.ValidationException

                            try {
                                val feed = json.decodeFromString<OpdsFeed>(jsonContent)
                                val feedResult = validator.validateFeed(feed)

                                if (feedResult.isSuccess) {
                                    println("Validation successful: The file is a valid OPDS feed.")
                                    return
                                } else {
                                    // Both failed, show publication errors since that was more likely
                                    println("Validation failed: The file is not a valid OPDS publication.")
                                    println("Errors:")
                                    exception?.errors?.forEach { println("- $it") }
                                }
                            } catch (e: SerializationException) {
                                // Only publication validation was possible, show those errors
                                println("Validation failed: The file is not a valid OPDS publication.")
                                println("Errors:")
                                exception?.errors?.forEach { println("- $it") }
                            }
                        }
                    } catch (e: SerializationException) {
                        // Try as feed instead
                        try {
                            val feed = json.decodeFromString<OpdsFeed>(jsonContent)
                            val result = validator.validateFeed(feed)

                            if (result.isSuccess) {
                                println("Validation successful: The file is a valid OPDS feed.")
                            } else {
                                val exception = result.exceptionOrNull() as? OpdsValidator.ValidationException
                                println("Validation failed: The file is not a valid OPDS feed.")
                                println("Errors:")
                                exception?.errors?.forEach { println("- $it") }
                            }
                        } catch (e2: SerializationException) {
                            println("Error: The file is not a valid OPDS feed or publication.")
                            println("Details: ${e2.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error: Invalid JSON structure in file")
                println("Details: ${e.message}")
            }
        } catch (e: Exception) {
            println("Error validating file: ${e.message}")
        }
    }

    /**
     * Determines if a JSON object is more likely to be a feed based on its structure.
     * @param jsonObject The JSON object to analyze
     * @return true if the object is more likely to be a feed, false otherwise
     */
    private fun isProbablyFeed(jsonObject: JsonObject): Boolean {
        // Check for feed-specific collections
        val hasNavigation = jsonObject.containsKey("navigation")
        val hasPublications = jsonObject.containsKey("publications")
        val hasGroups = jsonObject.containsKey("groups")
        val hasFacets = jsonObject.containsKey("facets")

        // If it has any feed-specific collections, it's likely a feed
        return hasNavigation || hasPublications || hasGroups || hasFacets
    }
}