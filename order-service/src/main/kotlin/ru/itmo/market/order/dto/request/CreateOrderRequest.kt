package ru.itmo.market.order.dto.request

import jakarta.validation.constraints.*
import java.math.BigDecimal

data class CreateOrderRequest(
    @field:Positive(message = "User ID must be positive")
    val userId: Long,

    @field:NotEmpty(message = "Order items cannot be empty")
    val items: List<OrderItemRequest>,

    @field:NotBlank(message = "Shipping address is required")
    @field:Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    val shippingAddress: String,

    @field:Size(max = 500, message = "Notes cannot exceed 500 characters")
    val notes: String? = null
)

data class OrderItemRequest(
    @field:Positive(message = "Product ID must be positive")
    val productId: Long,

    @field:Positive(message = "Quantity must be positive")
    val quantity: Int,

    @field:DecimalMin("0.01", message = "Price must be greater than 0")
    val price: BigDecimal
)
