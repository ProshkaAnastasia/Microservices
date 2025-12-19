package ru.itmo.market.product.dto.request

import jakarta.validation.constraints.*
import java.math.BigDecimal

data class CreateProductRequest(
    @field:NotBlank(message = "Product name is required")
    @field:Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    val name: String,

    @field:Size(max = 1000, message = "Description cannot exceed 1000 characters")
    val description: String? = null,

    @field:NotNull(message = "Price is required")
    @field:DecimalMin("0.01", message = "Price must be greater than 0")
    val price: BigDecimal,

    @field:Min(value = 0, message = "Quantity cannot be negative")
    val quantity: Int = 0,

    @field:NotNull(message = "Category ID is required")
    @field:Positive(message = "Category ID must be positive")
    val categoryId: Long,

    @field:Size(max = 255, message = "Image URL cannot exceed 255 characters")
    val imageUrl: String? = null
)
