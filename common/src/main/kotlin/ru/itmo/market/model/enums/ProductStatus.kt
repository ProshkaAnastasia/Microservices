package ru.itmo.market.model.enums

enum class ProductStatus(val displayName: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ARCHIVED("Archived"),
    DRAFT("Draft"),
    BLOCKED("Blocked");

    companion object {
        fun fromString(value: String): ProductStatus? = 
            values().find { it.name.equals(value, ignoreCase = true) }
    }
}
