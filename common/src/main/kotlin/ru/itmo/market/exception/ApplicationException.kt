package ru.itmo.market.exception

open class ApplicationException(
    val errorCode: String,
    override val message: String,
    val details: Map<String, Any> = emptyMap(),
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {
    companion object {
        const val INTERNAL_ERROR = "INTERNAL_ERROR"
        const val VALIDATION_ERROR = "VALIDATION_ERROR"
        const val RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND"
        const val CONFLICT_ERROR = "CONFLICT_ERROR"
        const val UNAUTHORIZED = "UNAUTHORIZED"
        const val FORBIDDEN = "FORBIDDEN"
    }
}
