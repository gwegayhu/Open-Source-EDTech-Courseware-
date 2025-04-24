// shared/src/jvmMain/kotlin/world/respect/OpdsCliApp.kt
package world.respect

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Paths
import world.respect.serialization.OpdsSerialization
import world.respect.validation.OpdsValidator

/**
 * Command line application for validating OPDS catalogs and publications.
 * This application can validate OPDS files without a user interface, as specified by Mike.
 */
object OpdsCliApp {

    /**
     * Prints help information for the application.
     */
    private fun printHelp() {
        println("OPDS Validator - Command Line Application")
        println("Usage: java -jar opds-validator.jar [options] <file>")
        println("Options:")
        println("  --catalog       Validate file as an OPDS catalog (default)")
        println("  --publication   Validate file as an OPDS publication")
        println("  --help          Display this help message")
        println("\nExample:")
        println("  java -jar opds-validator.jar --catalog catalog.json")
    }

    /**
     * Validates an OPDS catalog file.
     *
     * @param filePath The path to the OPDS catalog file
     */
    private fun validateCatalog(filePath: String) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                println("Error: File not found: $filePath")
                return
            }

            val jsonContent = file.readText()

            try {
                val catalog = OpdsSerialization.parseOpdsCatalog(jsonContent)

                val validator = OpdsValidator()
                val result = validator.validateCatalog(catalog)

                if (result.isSuccess) {
                    println("Validation successful: The OPDS catalog is valid.")
                } else {
                    val exception = result.exceptionOrNull() as? OpdsValidator.ValidationException
                    println("Validation failed with errors:")
                    exception?.errors?.forEach { println("- $it") }
                }
            } catch (e: SerializationException) {
                println("Error parsing JSON as OPDS catalog: ${e.message}")
                println("Tip: If this is a publication file, try using --publication instead of --catalog")
            }
        } catch (e: Exception) {
            println("Error validating catalog: ${e.message}")
        }
    }

    /**
     * Validates an OPDS publication file.
     *
     * @param filePath The path to the OPDS publication file
     */
    private fun validatePublication(filePath: String) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                println("Error: File not found: $filePath")
                return
            }

            val jsonContent = file.readText()

            try {
                val publication = OpdsSerialization.parsePublication(jsonContent)

                val validator = OpdsValidator()
                val result = validator.validatePublication(publication)

                if (result.isSuccess) {
                    println("Validation successful: The OPDS publication is valid.")
                } else {
                    val exception = result.exceptionOrNull() as? OpdsValidator.ValidationException
                    println("Validation failed with errors:")
                    exception?.errors?.forEach { println("- $it") }
                }
            } catch (e: SerializationException) {
                println("Error parsing JSON as OPDS publication: ${e.message}")
                println("Tip: If this is a catalog file, try using --catalog instead of --publication")
            }
        } catch (e: Exception) {
            println("Error validating publication: ${e.message}")
        }
    }

    /**
     * Main entry point for the command line application.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty() || args.contains("--help")) {
            printHelp()
            return
        }

        var filePath = args.last()
        val isPublication = args.contains("--publication")

        // Basic validation to ensure the file path is the last argument
        if (filePath.startsWith("--")) {
            println("Error: Missing file path")
            printHelp()
            return
        }

        // Get absolute path
        try {
            filePath = Paths.get(filePath).toAbsolutePath().toString()
        } catch (e: Exception) {
            println("Error with file path: ${e.message}")
            return
        }

        if (isPublication) {
            validatePublication(filePath)
        } else {
            validateCatalog(filePath)
        }
    }
}