package world.respect.datasource.ext

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import world.respect.datasource.DataErrorResult
import world.respect.datasource.DataLoadMetaInfo
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataReadyState
import world.respect.datasource.DataLoadState
import world.respect.datasource.DataLoadingState

suspend inline fun <reified T: Any> HttpClient.getAsDataLoadState(
    url: Url
): DataLoadState<T> {
    return try {
        val response = this.get(url)
        val data = response.body<T>()
        DataReadyState(
            data = data,
            metaInfo = DataLoadMetaInfo.fromHttpMessage(url, response),
        )
    }catch(e: Throwable) {
        DataErrorResult(
            error = e,
            metaInfo = DataLoadMetaInfo(
                url = url,
            )
        )
    }
}

@Suppress("unused")//reserved for future use
inline fun <reified T: Any> HttpClient.getDataLoadResultAsFlow(
    url: Url,
    dataLoadParams: DataLoadParams,
): Flow<DataLoadState<T>> {
    return flow {
        emit(
            DataLoadingState(
                metaInfo = DataLoadMetaInfo(url = url)
            )
        )

        emit(getAsDataLoadState(url))
    }
}
