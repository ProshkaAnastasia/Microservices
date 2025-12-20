package ru.itmo.market.order.entity

import jakarta.persistence.*
import ru.itmo.market.util.DateUtils
import java.math.BigDecimal
import java.time.LocalDateTime

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

@Entity
@Table(
    name = "orders",
    schema = "order_service",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_status", columnList = "status"),
        Index(name = "idx_created_at", columnList = "created_at DESC")
    ]
)
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var userId: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.PENDING,

    @Column(nullable = false, precision = 10, scale = 2)
    var totalPrice: BigDecimal = BigDecimal.ZERO,

    @Column(columnDefinition = "TEXT")
    var notes: String? = null,

    @Column(length = 255)
    var shippingAddress: String? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = DateUtils.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = DateUtils.now(),

    @Column(nullable = false)
    var isDeleted: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Order) return false
        return id == other.id && id != 0L
    }

    override fun hashCode(): Int = id.hashCode()
}
