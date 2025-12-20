package ru.itmo.market.order.client

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.stereotype.Component
import java.math.BigDecimal

@FeignClient(
    name = "product-service",
    fallback = ProductServiceClientFallback::class
)
interface ProductServiceClient {
    
    @GetMapping("/products/{id}")
    @CircuitBreaker(name = "productServiceCB", fallbackMethod = "getProductByIdFallback")
    fun getProductById(@PathVariable id: Long): ProductResponse?
}

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val quantity: Int
)

@Component
class ProductServiceClientFallback : ProductServiceClient {
    override fun getProductById(id: Long): ProductResponse? {
        println("Product Service is down, returning fallback for productId: $id")
        return null
    }
}
