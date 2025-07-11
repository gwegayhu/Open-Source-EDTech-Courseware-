package world.respect.datalayer.networkvalidation

import io.ktor.http.Url

/**
 * When a Network data source is sending a request, it needs to be able to retrieve the last-modified
 * date and etag from the previous received response (if any). This is then added to the request
 * (using if-modified-since and if-none-match) headers. If the data on the server has not been
 * modified, the server can reply 304 not modified. This then avoids unnecessary data transfer over
 * the network and local database updates (which would trigger invalidations on the database and
 * possibly UI work).
 *
 * This can happen in two ways:
 *
 * a) Individual entity level validation info: where the entity itself contains validation info, and
 *    there is a 1:1 relationship between the entity and the URL that the datasource requests.
 * b) List request validation info: where an API call is being made that returns a list of entities,
 *    then a separate table is used to store validation info per-URL.
 */
interface NetworkDataSourceValidationHelper {

    suspend fun getValidationInfo(url: Url): NetworkValidationInfo?

}