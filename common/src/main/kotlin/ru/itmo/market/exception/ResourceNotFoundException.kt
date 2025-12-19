package ru.itmo.market.exception

class ResourceNotFoundException(
    message: String,
    resource: String = "Resource",
    resourceId: Any? = null
) : ApplicationException(
    errorCode = RESOURCE_NOT_FOUND,
    message = message,
    details = mutableMapOf<String, Any>().apply {
        put("resource", resource)
        if (resourceId != null) put("id", resourceId)
    }
) {
    companion object {
        const val RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND"
    }
}
