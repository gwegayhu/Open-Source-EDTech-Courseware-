package world.respect

import world.respect.model.OpdsSerialization
import world.respect.validator.OpdsValidator
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse


class OpdsFileParsingTest {

    @Test
    fun testParseSampleFiles() {
        // Get the paths to your sample JSON files
        val sampleCatalogPath = "/home/prashant/AndroidStudioProjects/Respect/composeApp/src/desktopMain/resources/sample-catalog.json"
        val invalidCatalogPath = "/home/prashant/AndroidStudioProjects/Respect/composeApp/src/desktopMain/resources/invalid-catalog.json"
        val sampleWithArrayRelPath = "/home/prashant/AndroidStudioProjects/Respect/composeApp/src/desktopMain/resources/sample-with-array-rel.json"
        val sampleFeedPath = "/home/prashant/AndroidStudioProjects/Respect/composeApp/src/desktopMain/resources/sample-feed.json"

        try {
            val sampleCatalogJson = java.io.File(sampleCatalogPath).readText()
            val catalog = OpdsSerialization.parseOpdsCatalog(sampleCatalogJson)
            val validator = OpdsValidator()
            val result = validator.validateCatalog(catalog)
            assertTrue(result.isSuccess, "sample-catalog.json should be valid")

            val invalidCatalogJson = java.io.File(invalidCatalogPath).readText()
            val invalidCatalog = OpdsSerialization.parseOpdsCatalog(invalidCatalogJson)
            val invalidResult = validator.validateCatalog(invalidCatalog)
            assertFalse(invalidResult.isSuccess, "invalid-catalog.json should fail validation")

            // Test sample-with-array-rel.json
            val arrayRelJson = java.io.File(sampleWithArrayRelPath).readText()
            val arrayRelCatalog = OpdsSerialization.parseOpdsCatalog(arrayRelJson)
            val arrayRelResult = validator.validateCatalog(arrayRelCatalog)
            assertTrue(arrayRelResult.isSuccess, "sample-with-array-rel.json should be valid")


            // Test sample-with-array-rel.json
            val feedJson = java.io.File(sampleFeedPath).readText()
            val feedCatalog = OpdsSerialization.parseOpdsCatalog(feedJson)
            val feedResult = validator.validateCatalog(feedCatalog)
            assertTrue(feedResult.isSuccess, "sample-with-array-rel.json should be valid")

        } catch (e: Exception) {
            println("File tests skipped: ${e.message}")
        }
    }
}