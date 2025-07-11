package world.respect.datalayer.networkvalidation

import io.ktor.http.Url

/**
 * When a Network data source is sending a request, it needs to be able to retrieve the known
 * last-modified date, etag, etc from the previous response (if any). This can happen in two ways:
 *
 * a) Individual entity level validation info: where the entity itself contains validation info, and
 *    there is a 1:1 relationship between the entity and the URL that the datasource requests.
 * b) List request validation info: where an API call is being made that returns a list of entities,
 *    then a separate table is used to store validation info per-URL.
 */
interface NetworkDataSourceValidationHelper {

    suspend fun getValidationInfo(url: Url): NetworkValidationInfo?

}