package world.respect.datalayer.ext

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
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
    block: HttpRequestBuilder.() -> Unit = { },
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

            block()
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

inline fun <reified T: Any> HttpClient.getDataLoadResultAsFlow(
    url: Url,
    dataLoadParams: DataLoadParams,
    validationHelper: NetworkDataSourceValidationHelper? = null,
    crossinline block: HttpRequestBuilder.() -> Unit = { },
): Flow<DataLoadState<T>> {
    return getDataLoadResultAsFlow(
        urlFn = { url },
        dataLoadParams = dataLoadParams,
        validationHelper = validationHelper,
        block = block,
    )
}

/**
 * @param urlFn Data source functions often return a flow, however sometimes figuring out the url
 *        itself requires a suspended function (eg potentially a database lookup). The function
 *        that returns a flow itself is not  suspended. Accepting a parameter makes it easier to
 *        shift the operation to get the url into the flow.
 */
inline fun <reified T: Any> HttpClient.getDataLoadResultAsFlow(
    noinline urlFn: suspend () -> Url,
    @Suppress("unused") dataLoadParams: DataLoadParams,
    validationHelper: NetworkDataSourceValidationHelper? = null,
    crossinline block: HttpRequestBuilder.() -> Unit = { },
): Flow<DataLoadState<T>> {
    return flow {
        val urlVal = urlFn()
        emit(DataLoadingState(metaInfo = DataLoadMetaInfo(url = urlVal)))

        emit(getAsDataLoadState(urlVal, validationHelper, block = block))
    }
}

