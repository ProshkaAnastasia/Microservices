package ru.itmo.market.exception

class ValidationException(
    message: String,
    val field: String? = null,
    val rejectedValue: Any? = null,
    val validationRule: String? = null
) : ApplicationException(
    errorCode = VALIDATION_ERROR,
    message = message,
    details = mutableMapOf<String, Any>().apply {
        if (field != null) put("field", field)
        if (rejectedValue != null) put("rejectedValue", rejectedValue)
        if (validationRule != null) put("validationRule", validationRule)
    }
) {
    companion object {
        const val VALIDATION_ERROR = "VALIDATION_ERROR"
    }
}
