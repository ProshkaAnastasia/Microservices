package ru.itmo.market.product.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "categories",
    schema = "product_service",
    indexes = [
        Index(name = "idx_name", columnList = "name", unique = true)
    ]
)
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 100)
    var name: String = "",

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(length = 255)
    var imageUrl: String? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var isDeleted: Boolean = false,

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    var products: Set<Product> = setOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false
        return id == other.id && id != 0L
    }

    override fun hashCode(): Int = id.hashCode()
}
