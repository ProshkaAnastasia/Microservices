package ru.itmo.market.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    val error: ErrorDetails? = null
) {
    companion object {
        fun <T> success(data: T, message: String = "Success"): ApiResponse<T> = ApiResponse(
            success = true,
            data = data,
            message = message
        )

        fun <T> error(message: String, error: ErrorDetails?): ApiResponse<T> = ApiResponse(
            success = false,
            message = message,
            error = error
        )

        fun <T> error(message: String): ApiResponse<T> = ApiResponse(
            success = false,
            message = message
        )
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorDetails(
    val code: String? = null,
    val details: Map<String, Any>? = null
)
