package world.respect.datalayer.ext

import com.ustadmobile.ihttp.headers.asIHttpHeaders
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpHeaders
import io.ktor.http.toHttpDate
import io.ktor.util.date.GMTDate
import world.respect.datalayer.networkvalidation.BaseDataSourceValidationHelper

/**
 * Add if-modified-since and if-none-match headers to the request using the validationHelper.
 *
 * This MUST be called AFTER the url is set and any potential VARY headers are set.
 */
suspend fun HttpRequestBuilder.addCacheValidationHeaders(
    validationHelper: BaseDataSourceValidationHelper
) {
    val validationInfo = validationHelper.getValidationInfo(
        url = this.url.build(),
        requestHeaders = this.headers.build().asIHttpHeaders(),
    )

    validationInfo?.lastModified?.takeIf { it > 0 }?.also { lastMod ->
        headers[HttpHeaders.IfModifiedSince] = GMTDate(lastMod).toHttpDate()
    }

    validationInfo?.etag?.also { etag ->
        headers[HttpHeaders.IfNoneMatch] = etag
    }

}