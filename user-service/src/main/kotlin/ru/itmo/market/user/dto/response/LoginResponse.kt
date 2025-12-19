package ru.itmo.market.user.dto.response

data class LoginResponse(
    val user: UserResponse,
    val token: String,
    val expiresAt: Long
)
