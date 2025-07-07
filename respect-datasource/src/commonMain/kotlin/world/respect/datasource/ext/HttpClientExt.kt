package world.respect.datasource.ext

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import world.respect.datasource.DataErrorResult
import world.respect.datasource.DataLoadMetaInfo
import world.respect.datasource.DataLoadResult
import world.respect.datasource.DataLoadState
import world.respect.datasource.LoadingStatus

suspend inline fun <reified T: Any> HttpClient.getDataLoadResult(
    url: Url
): DataLoadState<T> {
    return try {
        val response = this.get(url)
        val data = response.body<T>()
        DataLoadResult(
            data = data,
            metaInfo = DataLoadMetaInfo.fromHttpMessage(url, response),
        )
    }catch(e: Throwable) {
        DataErrorResult(
            error = e,
            metaInfo = DataLoadMetaInfo(
                status = LoadingStatus.LOADED,
                url = url,
            )
        )
    }

}
