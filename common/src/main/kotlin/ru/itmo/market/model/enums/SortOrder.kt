package ru.itmo.market.model.enums

enum class SortOrder {
    ASC, DESC;

    companion object {
        fun fromString(value: String): SortOrder? = 
            values().find { it.name.equals(value, ignoreCase = true) }
    }
}
