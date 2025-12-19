package ru.itmo.market.user.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ChangePasswordRequest(
    @field:NotBlank(message = "Current password is required")
    val currentPassword: String,

    @field:NotBlank(message = "New password is required")
    @field:Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    val newPassword: String,

    @field:NotBlank(message = "Password confirmation is required")
    val confirmPassword: String
)
