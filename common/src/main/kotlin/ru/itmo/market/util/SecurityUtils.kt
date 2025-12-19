package ru.itmo.market.util

import java.security.MessageDigest
import java.util.*

object SecurityUtils {
    fun hashPassword(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = messageDigest.digest(password.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(hashedBytes)
    }

    fun verifyPassword(password: String, hash: String): Boolean {
        return hashPassword(password) == hash
    }

    fun generateToken(length: Int = 32): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun sanitizeInput(input: String): String {
        return input
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("&", "&amp;")
    }

    fun isValidUUID(uuid: String): Boolean {
        return try {
            UUID.fromString(uuid)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun maskEmail(email: String): String {
        val parts = email.split("@")
        if (parts.size != 2) return email
        
        val localPart = parts
        val domain = parts
        
        if (localPart.size <= 2) {
            return "*@$domain"
        }
        
        val masked = localPart + "*".repeat(localPart.size - 2) + localPart.last()
        return "$masked@$domain"
    }
}
