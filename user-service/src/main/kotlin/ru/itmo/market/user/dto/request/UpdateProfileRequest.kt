package ru.itmo.market.user.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UpdateProfileRequest(
    @field:Size(max = 100)
    val firstName: String? = null,

    @field:Size(max = 100)
    val lastName: String? = null,

    @field:Email
    @field:Size(max = 255)
    val email: String? = null,

    @field:Size(max = 255)
    val profileImageUrl: String? = null
)
