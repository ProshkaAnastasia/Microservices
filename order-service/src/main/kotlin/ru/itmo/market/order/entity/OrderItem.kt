package ru.itmo.market.order.entity

import jakarta.persistence.*
import ru.itmo.market.util.DateUtils
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(
    name = "order_items",
    schema = "order_service",
    indexes = [
        Index(name = "idx_order_id", columnList = "order_id"),
        Index(name = "idx_product_id", columnList = "product_id")
    ]
)
data class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order? = null,

    @Column(nullable = false)
    var productId: Long = 0,

    @Column(nullable = false)
    var quantity: Int = 0,

    @Column(nullable = false, precision = 10, scale = 2)
    var price: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = DateUtils.now(),

    @Column(nullable = false)
    var isDeleted: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderItem) return false
        return id == other.id && id != 0L
    }

    override fun hashCode(): Int = id.hashCode()
}
