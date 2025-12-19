package ru.itmo.market.product.dto.response

import ru.itmo.market.product.entity.Product
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val quantity: Int,
    val category: CategoryResponse,
    val imageUrl: String?,
    val rating: Double,
    val reviewCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(product: Product): ProductResponse = ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            price = product.price,
            quantity = product.quantity,
            category = CategoryResponse.fromEntity(product.category!!),
            imageUrl = product.imageUrl,
            rating = product.rating,
            reviewCount = product.reviewCount,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }
}
