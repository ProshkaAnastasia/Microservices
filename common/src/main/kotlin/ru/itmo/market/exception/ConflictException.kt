package ru.itmo.market.exception

class ConflictException(
    message: String,
    val conflictField: String? = null,
    val existingValue: Any? = null
) : ApplicationException(
    errorCode = CONFLICT_ERROR,
    message = message,
    details = mutableMapOf<String, Any>().apply {
        if (conflictField != null) put("field", conflictField)
        if (existingValue != null) put("existingValue", existingValue)
    }
) {
    companion object {
        const val CONFLICT_ERROR = "CONFLICT_ERROR"
    }
}
