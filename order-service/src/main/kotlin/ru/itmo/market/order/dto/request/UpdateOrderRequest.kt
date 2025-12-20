package ru.itmo.market.order.dto.request

import jakarta.validation.constraints.*
import ru.itmo.market.order.entity.OrderStatus

data class UpdateOrderRequest(
    @field:Size(max = 255, message = "Shipping address cannot exceed 255 characters")
    val shippingAddress: String? = null,

    @field:Size(max = 500, message = "Notes cannot exceed 500 characters")
    val notes: String? = null,

    val status: OrderStatus? = null
)
