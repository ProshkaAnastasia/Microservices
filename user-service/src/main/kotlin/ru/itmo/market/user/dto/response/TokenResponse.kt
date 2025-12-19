package ru.itmo.market.user.dto.response

data class TokenResponse(
    val token: String,
    val type: String = "Bearer",
    val expiresAt: Long
)
