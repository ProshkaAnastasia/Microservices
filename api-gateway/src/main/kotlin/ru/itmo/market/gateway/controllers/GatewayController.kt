package ru.itmo.market.gateway.controllers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RestController
class GatewayController {
    
    @GetMapping("/health")
    fun health(): Map<String, Any> {
        return mapOf(
            "status" to "UP",
            "service" to "API Gateway",
            "timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            "message" to "API Gateway is running"
        )
    }
    
    @GetMapping("/fallback/user-service")
    fun userServiceFallback(): Map<String, Any> {
        return mapOf(
            "error" to "User Service is currently unavailable",
            "fallback" to true,
            "service" to "user-service",
            "timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
    
    @GetMapping("/fallback/product-service")
    fun productServiceFallback(): Map<String, Any> {
        return mapOf(
            "error" to "Product Service is currently unavailable",
            "fallback" to true,
            "service" to "product-service",
            "timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
    
    @GetMapping("/fallback/shop-service")
    fun shopServiceFallback(): Map<String, Any> {
        return mapOf(
            "error" to "Shop Service is currently unavailable",
            "fallback" to true,
            "service" to "shop-service",
            "timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
    
    @GetMapping("/fallback/order-service")
    fun orderServiceFallback(): Map<String, Any> {
        return mapOf(
            "error" to "Order Service is currently unavailable",
            "fallback" to true,
            "service" to "order-service",
            "timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
    
    @GetMapping("/fallback/comment-service")
    fun commentServiceFallback(): Map<String, Any> {
        return mapOf(
            "error" to "Comment Service is currently unavailable",
            "fallback" to true,
            "service" to "comment-service",
            "timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
    
    @GetMapping("/fallback/cart-service")
    fun cartServiceFallback(): Map<String, Any> {
        return mapOf(
            "error" to "Cart Service is currently unavailable",
            "fallback" to true,
            "service" to "cart-service",
            "timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
    
    @GetMapping("/info")
    fun info(): Map<String, Any> {
        return mapOf(
            "app" to "ITMO-Market API Gateway",
            "version" to "0.0.1-SNAPSHOT",
            "timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            "routes" to listOf(
                mapOf(
                    "service" to "user-service",
                    "path" to "/api/users/**,/api/auth/**,/api/profiles/**",
                    "port" to 8081
                ),
                mapOf(
                    "service" to "product-service",
                    "path" to "/api/products/**,/api/search/**",
                    "port" to 8082
                ),
                mapOf(
                    "service" to "shop-service",
                    "path" to "/api/shops/**,/api/sellers/**",
                    "port" to 8083
                ),
                mapOf(
                    "service" to "order-service",
                    "path" to "/api/orders/**,/api/checkout/**",
                    "port" to 8084
                ),
                mapOf(
                    "service" to "comment-service",
                    "path" to "/api/comments/**,/api/reviews/**,/api/ratings/**",
                    "port" to 8085
                ),
                mapOf(
                    "service" to "cart-service",
                    "path" to "/api/cart/**",
                    "port" to 8086
                )
            )
        )
    }
}
