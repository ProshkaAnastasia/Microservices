package ru.itmo.market.user.service

import ru.itmo.market.user.entity.User
import ru.itmo.market.user.dto.request.CreateUserRequest
import ru.itmo.market.user.dto.request.UpdateProfileRequest
import ru.itmo.market.user.dto.request.ChangePasswordRequest
import ru.itmo.market.user.dto.response.UserResponse
import ru.itmo.market.user.repository.UserRepository
import ru.itmo.market.exception.ResourceNotFoundException
import ru.itmo.market.exception.ConflictException
import ru.itmo.market.exception.ValidationException
import ru.itmo.market.util.SecurityUtils
import ru.itmo.market.util.ValidationUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {
    
    fun createUser(request: CreateUserRequest): UserResponse {
        // Валидация
        ValidationUtils.validateNotBlank(request.email, "Email")
        ValidationUtils.validateEmail(request.email)
        ValidationUtils.validateNotBlank(request.username, "Username")
        ValidationUtils.validateMinLength(request.password, 6, "Password")
        
        // Проверка уникальности
        if (userRepository.findByEmailAndIsDeletedFalse(request.email).isPresent) {
            throw ConflictException(
                message = "Email already exists",
                conflictField = "email",
                existingValue = request.email
            )
        }
        
        if (userRepository.findByUsernameAndIsDeletedFalse(request.username).isPresent) {
            throw ConflictException(
                message = "Username already exists",
                conflictField = "username",
                existingValue = request.username
            )
        }
        
        // Создание пользователя
        val user = User(
            email = request.email.lowercase(),
            username = request.username,
            passwordHash = SecurityUtils.hashPassword(request.password),
            firstName = request.firstName,
            lastName = request.lastName,
            roles = request.roles
        )
        
        val savedUser = userRepository.save(user)
        return UserResponse.fromEntity(savedUser)
    }
    
    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found", "User", id) }
        
        if (user.isDeleted) {
            throw ResourceNotFoundException("User not found", "User", id)
        }
        
        return UserResponse.fromEntity(user)
    }
    
    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmailAndIsDeletedFalse(email)
            .orElseThrow { ResourceNotFoundException("User not found", "User", email) }
        
        return UserResponse.fromEntity(user)
    }
    
    @Transactional(readOnly = true)
    fun getUserByUsername(username: String): UserResponse {
        val user = userRepository.findByUsernameAndIsDeletedFalse(username)
            .orElseThrow { ResourceNotFoundException("User not found", "User", username) }
        
        return UserResponse.fromEntity(user)
    }
    
    @Transactional(readOnly = true)
    fun getUserByUsernameOrEmail(identifier: String): UserResponse {
        val user = userRepository.findByUsernameAndIsDeletedFalse(identifier)
            .orElseGet {
                userRepository.findByEmailAndIsDeletedFalse(identifier)
                    .orElse(null)
            }
        
        if (user == null || user.isDeleted) {
            throw ResourceNotFoundException("User not found", "User", identifier)
        }
        
        return UserResponse.fromEntity(user)
    }
    
    fun updateProfile(userId: Long, request: UpdateProfileRequest): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found", "User", userId) }
        
        if (user.isDeleted) {
            throw ResourceNotFoundException("User not found", "User", userId)
        }
        
        // Проверка изменения email
        request.email?.let { newEmail ->
            if (newEmail != user.email && userRepository.findByEmailAndIsDeletedFalse(newEmail).isPresent) {
                throw ConflictException(
                    message = "Email already exists",
                    conflictField = "email",
                    existingValue = newEmail
                )
            }
            user.email = newEmail.lowercase()
        }
        
        request.firstName?.let { user.firstName = it }
        request.lastName?.let { user.lastName = it }
        request.profileImageUrl?.let { user.profileImageUrl = it }
        
        val updatedUser = userRepository.save(user)
        return UserResponse.fromEntity(updatedUser)
    }
    
    fun changePassword(userId: Long, request: ChangePasswordRequest): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found", "User", userId) }
        
        if (user.isDeleted) {
            throw ResourceNotFoundException("User not found", "User", userId)
        }
        
        // Проверка текущего пароля
        if (!SecurityUtils.verifyPassword(request.currentPassword, user.passwordHash)) {
            throw ValidationException(
                message = "Current password is incorrect",
                field = "currentPassword"
            )
        }
        
        // Проверка совпадения паролей
        if (request.newPassword != request.confirmPassword) {
            throw ValidationException(
                message = "Passwords do not match",
                field = "confirmPassword"
            )
        }
        
        user.passwordHash = SecurityUtils.hashPassword(request.newPassword)
        val updatedUser = userRepository.save(user)
        return UserResponse.fromEntity(updatedUser)
    }
    
    @Transactional(readOnly = true)
    fun getAllUsers(pageable: Pageable): Page<UserResponse> {
        return userRepository.findAllActive(pageable)
            .map { UserResponse.fromEntity(it) }
    }
    
    @Transactional(readOnly = true)
    fun searchUsers(searchTerm: String, pageable: Pageable): Page<UserResponse> {
        return userRepository.findBySearchTerm(searchTerm, pageable)
            .map { UserResponse.fromEntity(it) }
    }
    
    fun deactivateUser(userId: Long): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found", "User", userId) }
        
        if (user.isDeleted) {
            throw ResourceNotFoundException("User not found", "User", userId)
        }
        
        user.isActive = false
        val updatedUser = userRepository.save(user)
        return UserResponse.fromEntity(updatedUser)
    }
    
    fun deleteUser(userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found", "User", userId) }
        
        user.isDeleted = true
        userRepository.save(user)
    }
    
    @Transactional(readOnly = true)
    fun verifyCredentials(identifier: String, password: String): UserResponse? {
        val user = userRepository.findByUsernameAndIsDeletedFalse(identifier)
            .orElseGet {
                userRepository.findByEmailAndIsDeletedFalse(identifier)
                    .orElse(null)
            }
        
        if (user == null || user.isDeleted || !user.isActive) {
            return null
        }
        
        return if (SecurityUtils.verifyPassword(password, user.passwordHash)) {
            UserResponse.fromEntity(user)
        } else {
            null
        }
    }
}
