package world.respect.validation


import world.respect.serialization.OpdsSerialization

/**
 * Utility class for testing the OpdsValidator.
 * This class can be used in both the main source set and test source set.
 */
object OpdsValidatorTestUtils {

    // Valid OPDS Catalog JSON sample
    private val validCatalogJson = """
    {
      "metadata": {
        "title": "Example OPDS Catalog"
      },
      "links": [
        {"rel": "self", "href": "http://example.com/opds", "type": "application/opds+json"}
      ],
      "navigation": [
        {
          "href": "/new", 
          "title": "New Learning Units", 
          "type": "application/opds+json", 
          "rel": "current"
        },
        {
          "href": "/popular", 
          "title": "Popular Learning Units", 
          "type": "application/opds+json", 
          "rel": "http://opds-spec.org/sort/popular"
        }
      ]
    }
    """.trimIndent()

    // Invalid OPDS Catalog without self link
    private val invalidCatalogNoSelfLinkJson = """
    {
      "metadata": {
        "title": "Example OPDS Catalog"
      },
      "links": [
        {"rel": "alternate", "href": "http://example.com/opds", "type": "application/opds+json"}
      ],
      "navigation": [
        {
          "href": "/new", 
          "title": "New Learning Units", 
          "type": "application/opds+json", 
          "rel": "current"
        }
      ]
    }
    """.trimIndent()

    // Valid OPDS Publication JSON sample
    private val validPublicationJson = """
    {
      "metadata": {
        "title": "Introduction to Programming",
        "@type": "http://schema.org/Course",
        "identifier": "urn:uuid:12345-67890",
        "language": "en",
        "description": "A comprehensive introduction to programming concepts."
      },
      "links": [
        {
          "href": "http://example.org/publication.json", 
          "rel": "self", 
          "type": "application/opds-publication+json"
        },
        {
          "href": "http://example.org/content/intro-programming", 
          "rel": "http://opds-spec.org/acquisition/open-access", 
          "type": "text/html"
        }
      ],
      "images": [
        {
          "href": "http://example.org/cover.jpg", 
          "type": "image/jpeg", 
          "height": 1400, 
          "width": 800
        }
      ]
    }
    """.trimIndent()

    /**
     * Runs a series of tests on the OPDS validator and prints the results.
     * This function can be called from a main function to manually test the validator.
     */
    fun runTests() {
        println("Testing OPDS Validator...")
        val validator = OpdsValidator()

        try {
            // Test valid catalog
            val validCatalog = OpdsSerialization.parseOpdsCatalog(validCatalogJson)
            val validCatalogResult = validator.validateCatalog(validCatalog)
            println("Valid catalog test: ${if (validCatalogResult.isSuccess) "PASSED" else "FAILED"}")

            // Test invalid catalog (no self link)
            val invalidCatalogNoSelfLink = OpdsSerialization.parseOpdsCatalog(invalidCatalogNoSelfLinkJson)
            val invalidCatalogNoSelfLinkResult = validator.validateCatalog(invalidCatalogNoSelfLink)
            println("Invalid catalog (no self link) test: ${if (!invalidCatalogNoSelfLinkResult.isSuccess) "PASSED" else "FAILED"}")
            if (!invalidCatalogNoSelfLinkResult.isSuccess) {
                println("Errors: ${(invalidCatalogNoSelfLinkResult.exceptionOrNull() as? OpdsValidator.ValidationException)?.errors}")
            }

            // Test valid publication
            val validPublication = OpdsSerialization.parsePublication(validPublicationJson)
            val validPublicationResult = validator.validatePublication(validPublication)
            println("Valid publication test: ${if (validPublicationResult.isSuccess) "PASSED" else "FAILED"}")

            println("All tests completed.")
        } catch (e: Exception) {
            println("Test failed with exception: ${e.message}")
            e.printStackTrace()
        }
    }
}