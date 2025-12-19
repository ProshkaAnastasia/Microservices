package ru.itmo.market.product.dto.response

import ru.itmo.market.product.entity.Category
import java.time.LocalDateTime

data class CategoryResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(category: Category): CategoryResponse = CategoryResponse(
            id = category.id,
            name = category.name,
            description = category.description,
            imageUrl = category.imageUrl,
            createdAt = category.createdAt,
            updatedAt = category.updatedAt
        )
    }
}
