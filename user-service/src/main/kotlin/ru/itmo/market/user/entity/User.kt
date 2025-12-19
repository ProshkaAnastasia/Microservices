package ru.itmo.market.user.entity

import ru.itmo.market.model.enums.UserRole
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users", schema = "user_service", indexes = [
    Index(name = "idx_email", columnList = "email", unique = true),
    Index(name = "idx_username", columnList = "username", unique = true),
    Index(name = "idx_deleted", columnList = "is_deleted")
])
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 255)
    var email: String = "",

    @Column(nullable = false, unique = true, length = 100)
    var username: String = "",

    @Column(nullable = false, length = 255)
    var passwordHash: String = "",

    @Column(length = 100)
    var firstName: String? = null,

    @Column(length = 100)
    var lastName: String? = null,

    @Column(length = 255)
    var profileImageUrl: String? = null,

    @ElementCollection(fetch = FetchType.EAGER, targetClass = UserRole::class)
    @CollectionTable(name = "user_roles", schema = "user_service", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var roles: Set<UserRole> = setOf(UserRole.USER),

    @Column(nullable = false)
    var isActive: Boolean = true,

    @Column(nullable = false)
    var emailVerified: Boolean = false,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var isDeleted: Boolean = false
) {
    fun getFullName(): String = 
        listOfNotNull(firstName, lastName).joinToString(" ")
            .takeIf { it.isNotBlank() } ?: username

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id && id != 0L
    }

    override fun hashCode(): Int = id.hashCode()
}
