package ru.itmo.market.user.dto.request

import ru.itmo.market.model.enums.UserRole
import jakarta.validation.constraints.*

data class CreateUserRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    @field:Size(max = 255)
    val email: String,

    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, hyphens and underscores")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    val password: String,

    @field:Size(max = 100)
    val firstName: String? = null,

    @field:Size(max = 100)
    val lastName: String? = null,

    val roles: Set<UserRole> = setOf(UserRole.USER)
)
