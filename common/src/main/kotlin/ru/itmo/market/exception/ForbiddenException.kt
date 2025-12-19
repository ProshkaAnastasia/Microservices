package ru.itmo.market.exception

class ForbiddenException(
    message: String,
    val resource: String = "Resource",
    val userId: Long? = null
) : ApplicationException(
    errorCode = FORBIDDEN,
    message = message,
    details = mutableMapOf<String, Any>().apply {
        put("resource", resource)
        if (userId != null) put("userId", userId)
    }
) {
    companion object {
        const val FORBIDDEN = "FORBIDDEN"
    }
}
