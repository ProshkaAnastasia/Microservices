package ru.itmo.market.util

import ru.itmo.market.exception.ValidationException

object ValidationUtils {
    fun validateNotBlank(value: String?, field: String = "field") {
        if (value.isNullOrBlank()) {
            throw ValidationException(
                message = "$field cannot be empty",
                field = field,
                rejectedValue = value,
                validationRule = "NotBlank"
            )
        }
    }

    fun validateEmail(email: String) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"
        if (!email.matches(emailRegex.toRegex())) {
            throw ValidationException(
                message = "Invalid email format",
                field = "email",
                rejectedValue = email,
                validationRule = "Email"
            )
        }
    }

    fun validatePositive(value: Long?, field: String = "field") {
        if (value == null || value <= 0) {
            throw ValidationException(
                message = "$field must be positive",
                field = field,
                rejectedValue = value,
                validationRule = "Positive"
            )
        }
    }

    fun validateRange(value: Int?, min: Int, max: Int, field: String = "field") {
        if (value == null || value < min || value > max) {
            throw ValidationException(
                message = "$field must be between $min and $max",
                field = field,
                rejectedValue = value,
                validationRule = "Range"
            )
        }
    }

    fun validateMinLength(value: String?, minLength: Int, field: String = "field") {
        if (value == null || value.length < minLength) {
            throw ValidationException(
                message = "$field must be at least $minLength characters",
                field = field,
                rejectedValue = value,
                validationRule = "MinLength"
            )
        }
    }

    fun validateMaxLength(value: String?, maxLength: Int, field: String = "field") {
        if (value != null && value.length > maxLength) {
            throw ValidationException(
                message = "$field must not exceed $maxLength characters",
                field = field,
                rejectedValue = value,
                validationRule = "MaxLength"
            )
        }
    }
}
