package ru.itmo.market.model.enums

enum class UserRole(val displayName: String) {
    ADMIN("Administrator"),
    SELLER("Seller"),
    BUYER("Buyer"),
    MODERATOR("Moderator"),
    USER("User");

    companion object {
        fun fromString(value: String): UserRole? = 
            values().find { it.name.equals(value, ignoreCase = true) }
    }
}
