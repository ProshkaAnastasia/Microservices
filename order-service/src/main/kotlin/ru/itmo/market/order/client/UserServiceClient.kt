package ru.itmo.market.order.client

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.stereotype.Component

@FeignClient(
    name = "user-service",
    fallback = UserServiceClientFallback::class
)
interface UserServiceClient {
    
    @GetMapping("/users/{id}")
    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "getUserByIdFallback")
    fun getUserById(@PathVariable id: Long): UserResponse?
}

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val isActive: Boolean
)

@Component
class UserServiceClientFallback : UserServiceClient {
    override fun getUserById(id: Long): UserResponse? {
        println("User Service is down, returning fallback for userId: $id")
        return null
    }
}
