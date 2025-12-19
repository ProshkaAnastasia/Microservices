package ru.itmo.market.product.controller

import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.market.product.dto.request.CreateProductRequest
import ru.itmo.market.product.dto.request.UpdateProductRequest
import ru.itmo.market.product.dto.response.ProductResponse
import ru.itmo.market.product.service.ProductService

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = ["*"])
class ProductController(
    private val productService: ProductService
) {
    
    @PostMapping
    fun createProduct(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<ProductResponse> {
        val product = productService.createProduct(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(product)
    }
    
    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductResponse> {
        val product = productService.getProductById(id)
        return ResponseEntity.ok(product)
    }
    
    @GetMapping
    fun getAllProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Map<String, Any>> {
        val pageable = PageRequest.of(page, size)
        val products = productService.getAllProducts(pageable)
        return ResponseEntity.ok(mapOf(
            "items" to products.content,
            "page" to (page + 1),
            "pageSize" to size,
            "totalElements" to products.totalElements
        ))
    }
    
    @GetMapping("/search")
    fun searchProducts(
        @RequestParam searchTerm: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Map<String, Any>> {
        val pageable = PageRequest.of(page, size)
        val products = productService.searchProducts(searchTerm, pageable)
        return ResponseEntity.ok(mapOf(
            "items" to products.content,
            "page" to (page + 1),
            "pageSize" to size,
            "totalElements" to products.totalElements
        ))
    }
    
    @GetMapping("/category/{categoryId}")
    fun getProductsByCategory(
        @PathVariable categoryId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Map<String, Any>> {
        val pageable = PageRequest.of(page, size)
        val products = productService.getProductsByCategory(categoryId, pageable)
        return ResponseEntity.ok(mapOf(
            "items" to products.content,
            "page" to (page + 1),
            "pageSize" to size,
            "totalElements" to products.totalElements
        ))
    }
    
    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateProductRequest
    ): ResponseEntity<ProductResponse> {
        val product = productService.updateProduct(id, request)
        return ResponseEntity.ok(product)
    }
    
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        productService.deleteProduct(id)
        return ResponseEntity.ok(mapOf("message" to "Product deleted successfully"))
    }
    
    @GetMapping("/{id}/async")
    fun getProductByIdAsync(@PathVariable id: Long): Mono<ProductResponse> {
        return productService.getProductByIdAsync(id)
    }
    
    @PostMapping("/async")
    fun createProductAsync(@Valid @RequestBody request: CreateProductRequest): Mono<ProductResponse> {
        return productService.createProductAsync(request)
    }
    
    @GetMapping("/search/async")
    fun searchProductsAsync(
        @RequestParam searchTerm: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): Flux<ProductResponse> {
        val pageable = PageRequest.of(page, size)
        return productService.searchProductsAsync(searchTerm, pageable)
    }
}
