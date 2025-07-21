package com.ustadmobile.libcache.headers

import com.ustadmobile.ihttp.headers.IHttpHeader
import kotlinx.datetime.format
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.format.DateTimeComponents

@OptIn(ExperimentalTime::class)
fun lastModifiedHeader(
    time: Long = 0
): IHttpHeader {
    return IHttpHeader.fromNameAndValue(
        name = "Last-Modified",
        value = Instant.fromEpochMilliseconds(time)
            .format(DateTimeComponents.Formats.RFC_1123)
    )
}
