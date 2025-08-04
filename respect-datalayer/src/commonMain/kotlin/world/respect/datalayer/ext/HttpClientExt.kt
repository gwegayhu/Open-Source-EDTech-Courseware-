package world.respect.datalayer.ext

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.toHttpDate
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import world.respect.datalayer.DataErrorResult
import world.respect.datalayer.DataLoadMetaInfo
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataReadyState
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataLoadingState
import world.respect.datalayer.NoDataLoadedState
import world.respect.datalayer.networkvalidation.NetworkDataSourceValidationHelper
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
suspend inline fun <reified T: Any> HttpClient.getAsDataLoadState(
    url: Url,
    validationHelper: NetworkDataSourceValidationHelper? = null,
): DataLoadState<T> {
    return try {
        //see https://github.com/Kotlin/kotlinx-datetime/issues/564
        val validationInfo = validationHelper?.getValidationInfo(url)
        val response = this.get(url) {
            validationInfo?.lastModified?.takeIf { it > 0 }?.also { lastMod ->
                headers[HttpHeaders.IfModifiedSince] = GMTDate(lastMod).toHttpDate()
            }

            validationInfo?.etag?.also { etag ->
                headers[HttpHeaders.IfNoneMatch] = etag
            }
        }

        return if(response.status == HttpStatusCode.NotModified) {
            NoDataLoadedState.notModified()
        }else {
            val data = response.body<T>()
            DataReadyState(
                data = data,
                metaInfo = DataLoadMetaInfo.fromHttpMessage(url, response),
            )
        }
    }catch(e: Throwable) {
        DataErrorResult(
            error = e,
            metaInfo = DataLoadMetaInfo(url = url)
        )
    }
}

@Suppress("unused")//reserved for future use
inline fun <reified T: Any> HttpClient.getDataLoadResultAsFlow(
    url: Url,
    dataLoadParams: DataLoadParams,
    validationHelper: NetworkDataSourceValidationHelper? = null,
): Flow<DataLoadState<T>> {
    return flow {
        emit(DataLoadingState(metaInfo = DataLoadMetaInfo(url = url)))

        emit(getAsDataLoadState(url, validationHelper))
    }
}
