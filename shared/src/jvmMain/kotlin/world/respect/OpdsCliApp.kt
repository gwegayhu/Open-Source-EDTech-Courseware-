package world.respect

import kotlinx.serialization.SerializationException
import world.respect.model.OpdsSerialization
import java.io.File
import java.nio.file.Paths
import world.respect.validator.OpdsValidator

/**
 * Command line application for validating OPDS files.
 * This application automatically detects whether a file is an OPDS catalog or publication
 * and validates it according to the OPDS 2.0 specification.
 */
object OpdsCliApp {

    /**
     * Prints help information for the application.
     */
    private fun printHelp() {
        println("OPDS Validator - Command Line Application")
        println("Usage: java -jar opds-validator.jar <file>")
        println("\nExample:")
        println("  java -jar opds-validator.jar catalog.json")
        println("\n Validates OPDS 2.0 files used for listing learning units in RESPECT.")
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
     * Validates an OPDS file, determining if it's a valid catalog or publication.
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
            var isValid = false

            // Try as catalog first
            try {
                val catalog = OpdsSerialization.parseOpdsCatalog(jsonContent)
                val validator = OpdsValidator()
                val result = validator.validateCatalog(catalog)

                if (result.isSuccess) {
                    println("Validation successful: The file is a valid OPDS catalog.")
                    isValid = true
                } else {
                    val exception = result.exceptionOrNull() as? OpdsValidator.ValidationException
                    println("OPDS catalog validation failed with errors:")
                    exception?.errors?.forEach { println("- $it") }
                }
            } catch (e: SerializationException) {
                // If it fails as a catalog, don't show the error yet
            }

            // If not valid as a catalog, try as a publication
            if (!isValid) {
                try {
                    val publication = OpdsSerialization.parsePublication(jsonContent)
                    val validator = OpdsValidator()
                    val result = validator.validatePublication(publication)

                    if (result.isSuccess) {
                        println("Validation successful: The file is a valid OPDS publication.")
                        isValid = true
                    } else {
                        val exception = result.exceptionOrNull() as? OpdsValidator.ValidationException
                        println("OPDS publication validation failed with errors:")
                        exception?.errors?.forEach { println("- $it") }
                    }
                } catch (e: SerializationException) {
                    // If both fail, show a general error
                    if (!isValid) {
                        println("Error: The file is not a valid OPDS catalog or publication.")
                        println("Details: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            println("Error validating file: ${e.message}")
        }
    }
}