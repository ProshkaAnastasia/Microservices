package ru.itmo.market.product.dto.request

import jakarta.validation.constraints.*

data class CreateCategoryRequest(
    @field:NotBlank(message = "Category name is required")
    @field:Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    val name: String,

    @field:Size(max = 1000, message = "Description cannot exceed 1000 characters")
    val description: String? = null,

    @field:Size(max = 255, message = "Image URL cannot exceed 255 characters")
    val imageUrl: String? = null
)
