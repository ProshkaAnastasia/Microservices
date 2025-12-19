package ru.itmo.market.user.controller

import ru.itmo.market.user.dto.request.CreateUserRequest
import ru.itmo.market.user.dto.request.UpdateProfileRequest
import ru.itmo.market.user.dto.request.ChangePasswordRequest
import ru.itmo.market.user.dto.response.UserResponse
import ru.itmo.market.user.service.UserService
import ru.itmo.market.model.dto.response.ApiResponse
import ru.itmo.market.model.dto.response.PaginatedResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    
    @PostMapping
    fun createUser(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.createUser(request)
        return ResponseEntity(
            ApiResponse.success(user, "User created successfully"),
            HttpStatus.CREATED
        )
    }
    
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(ApiResponse.success(user))
    }
    
    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.getUserByEmail(email)
        return ResponseEntity.ok(ApiResponse.success(user))
    }
    
    @GetMapping("/username/{username}")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.getUserByUsername(username)
        return ResponseEntity.ok(ApiResponse.success(user))
    }
    
    @GetMapping("/identifier/{identifier}")
    fun getUserByIdentifier(@PathVariable identifier: String): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.getUserByUsernameOrEmail(identifier)
        return ResponseEntity.ok(ApiResponse.success(user))
    }
    
    @PutMapping("/{id}")
    fun updateProfile(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateProfileRequest
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.updateProfile(id, request)
        return ResponseEntity.ok(ApiResponse.success(user, "Profile updated successfully"))
    }
    
    @PostMapping("/{id}/change-password")
    fun changePassword(
        @PathVariable id: Long,
        @Valid @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.changePassword(id, request)
        return ResponseEntity.ok(ApiResponse.success(user, "Password changed successfully"))
    }
    
    @GetMapping
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<PaginatedResponse<UserResponse>>> {
        val pageable = PageRequest.of(page, size)
        val users = userService.getAllUsers(pageable)
        val response = PaginatedResponse.of(
            users.content,
            page + 1,
            size,
            users.totalElements
        )
        return ResponseEntity.ok(ApiResponse.success(response))
    }
    
    @GetMapping("/search")
    fun searchUsers(
        @RequestParam searchTerm: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<PaginatedResponse<UserResponse>>> {
        val pageable = PageRequest.of(page, size)
        val users = userService.searchUsers(searchTerm, pageable)
        val response = PaginatedResponse.of(
            users.content,
            page + 1,
            size,
            users.totalElements
        )
        return ResponseEntity.ok(ApiResponse.success(response))
    }
    
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<ApiResponse<String>> {
        userService.deleteUser(id)
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"))
    }
    
    @PutMapping("/{id}/deactivate")
    fun deactivateUser(@PathVariable id: Long): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.deactivateUser(id)
        return ResponseEntity.ok(ApiResponse.success(user, "User deactivated successfully"))
    }
    
    @PostMapping("/verify-credentials")
    fun verifyCredentials(
        @RequestParam identifier: String,
        @RequestParam password: String
    ): ResponseEntity<ApiResponse<UserResponse?>> {
        val user = userService.verifyCredentials(identifier, password)
        return if (user != null) {
            ResponseEntity.ok(ApiResponse.success(user, "Credentials verified"))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid credentials", null))
        }
    }
}
