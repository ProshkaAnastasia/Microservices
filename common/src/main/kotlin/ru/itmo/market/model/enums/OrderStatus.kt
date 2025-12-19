package ru.itmo.market.model.enums

enum class OrderStatus(val displayName: String) {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    RETURNED("Returned");

    companion object {
        fun fromString(value: String): OrderStatus? = 
            values().find { it.name.equals(value, ignoreCase = true) }
    }
}
