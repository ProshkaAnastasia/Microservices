package ru.itmo.market.order.dto.response

import ru.itmo.market.order.entity.Order
import ru.itmo.market.order.entity.OrderStatus
import ru.itmo.market.util.DateUtils
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse(
    val id: Long,
    val userId: Long,
    val status: OrderStatus,
    val totalPrice: BigDecimal,
    val items: List<OrderItemResponse>,
    val shippingAddress: String?,
    val notes: String?,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun fromEntity(order: Order, items: List<OrderItemResponse>): OrderResponse = OrderResponse(
            id = order.id,
            userId = order.userId,
            status = order.status,
            totalPrice = order.totalPrice,
            items = items,
            shippingAddress = order.shippingAddress,
            notes = order.notes,
            createdAt = DateUtils.formatForDisplay(order.createdAt),
            updatedAt = DateUtils.formatForDisplay(order.updatedAt)
        )
    }
}

data class OrderItemResponse(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val price: BigDecimal
)
