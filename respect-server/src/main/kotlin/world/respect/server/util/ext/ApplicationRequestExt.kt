package world.respect.server.util.ext

import com.ustadmobile.ihttp.ext.clientProtocolAndHost
import com.ustadmobile.ihttp.headers.asIHttpHeaders
import io.ktor.http.Url
import io.ktor.server.request.ApplicationRequest

val ApplicationRequest.virtualHost: Url
    get() = Url(headers.asIHttpHeaders().clientProtocolAndHost())
