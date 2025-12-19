package ru.itmo.market.exception

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    val errorCode: String,
    val message: String,
    val path: String? = null,
    val details: Map<String, Any>? = null,
    val status: Int? = null
) {
    companion object {
        fun fromException(
            exception: ApplicationException,
            path: String? = null,
            status: Int? = null
        ) = ErrorResponse(
            errorCode = exception.errorCode,
            message = exception.message,
            path = path,
            details = if (exception.details.isNotEmpty()) exception.details else null,
            status = status
        )

        fun fromString(
            errorCode: String,
            message: String,
            path: String? = null,
            status: Int? = null
        ) = ErrorResponse(
            errorCode = errorCode,
            message = message,
            path = path,
            status = status
        )
    }
}
