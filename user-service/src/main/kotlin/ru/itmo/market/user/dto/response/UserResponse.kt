package ru.itmo.market.user.dto.response

import ru.itmo.market.user.entity.User
import ru.itmo.market.model.enums.UserRole
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val fullName: String,
    val profileImageUrl: String?,
    val roles: Set<UserRole>,
    val isActive: Boolean,
    val emailVerified: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(user: User): UserResponse = UserResponse(
            id = user.id,
            email = user.email,
            username = user.username,
            firstName = user.firstName,
            lastName = user.lastName,
            fullName = user.getFullName(),
            profileImageUrl = user.profileImageUrl,
            roles = user.roles,
            isActive = user.isActive,
            emailVerified = user.emailVerified,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}
