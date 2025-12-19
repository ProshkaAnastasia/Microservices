package ru.itmo.market.product.client

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "user-service",
    url = "\${user-service.url:http://localhost:8082/api/v1}",
    fallback = UserServiceClientFallback::class
)
interface UserServiceClient {
    
    @GetMapping("/users/{id}")
    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "getUserByIdFallback")
    fun getUserById(@PathVariable id: Long): UserResponse?
    
    @GetMapping("/users/username/{username}")
    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "getUserByUsernameFallback")
    fun getUserByUsername(@PathVariable username: String): UserResponse?
}

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val fullName: String,
    val isActive: Boolean
)

class UserServiceClientFallback : UserServiceClient {
    override fun getUserById(id: Long): UserResponse? {
        println("User Service is down, returning fallback for userId: $id")
        return null
    }

    override fun getUserByUsername(username: String): UserResponse? {
        println("User Service is down, returning fallback for username: $username")
        return null
    }
}
