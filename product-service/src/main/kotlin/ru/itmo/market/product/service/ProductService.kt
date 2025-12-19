package ru.itmo.market.product.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.itmo.market.product.client.UserServiceClient
import ru.itmo.market.product.dto.request.CreateProductRequest
import ru.itmo.market.product.dto.request.UpdateProductRequest
import ru.itmo.market.product.dto.response.ProductResponse
import ru.itmo.market.product.entity.Product
import ru.itmo.market.product.repository.CategoryRepository
import ru.itmo.market.product.repository.ProductRepository
import java.time.LocalDateTime

@Service
@Transactional
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val userServiceClient: UserServiceClient
) {
    
    fun createProductAsync(request: CreateProductRequest): Mono<ProductResponse> {
        return Mono.fromCallable {
            val category = categoryRepository.findById(request.categoryId)
                .orElseThrow { throw RuntimeException("Category not found") }
            
            val product = Product(
                name = request.name,
                description = request.description,
                price = request.price,
                quantity = request.quantity,
                category = category,
                imageUrl = request.imageUrl
            )
            
            productRepository.save(product)
        }
        .subscribeOn(Schedulers.boundedElastic())
        .map { ProductResponse.fromEntity(it) }
    }
    
    fun createProduct(request: CreateProductRequest): ProductResponse {
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { throw RuntimeException("Category not found") }
        
        val product = Product(
            name = request.name,
            description = request.description,
            price = request.price,
            quantity = request.quantity,
            category = category,
            imageUrl = request.imageUrl
        )
        
        val savedProduct = productRepository.save(product)
        return ProductResponse.fromEntity(savedProduct)
    }
    
    @Transactional(readOnly = true)
    fun getProductByIdAsync(id: Long): Mono<ProductResponse> {
        return Mono.fromCallable {
            productRepository.findById(id)
                .filter { !it.isDeleted }
                .orElseThrow { throw RuntimeException("Product not found") }
        }
        .subscribeOn(Schedulers.boundedElastic())
        .map { ProductResponse.fromEntity(it) }
    }
    
    @Transactional(readOnly = true)
    fun getProductById(id: Long): ProductResponse {
        val product = productRepository.findById(id)
            .filter { !it.isDeleted }
            .orElseThrow { throw RuntimeException("Product not found") }
        
        return ProductResponse.fromEntity(product)
    }
    
    @Transactional(readOnly = true)
    fun getAllProducts(pageable: Pageable): Page<ProductResponse> {
        return productRepository.findAllActive(pageable)
            .map { ProductResponse.fromEntity(it) }
    }
    
    @Transactional(readOnly = true)
    fun searchProductsAsync(searchTerm: String, pageable: Pageable): Flux<ProductResponse> {
        return Flux.fromIterable(
            productRepository.findBySearchTerm(searchTerm, pageable).content
        )
        .subscribeOn(Schedulers.boundedElastic())
        .map { ProductResponse.fromEntity(it) }
    }
    
    @Transactional(readOnly = true)
    fun searchProducts(searchTerm: String, pageable: Pageable): Page<ProductResponse> {
        return productRepository.findBySearchTerm(searchTerm, pageable)
            .map { ProductResponse.fromEntity(it) }
    }
    
    @Transactional(readOnly = true)
    fun getProductsByCategory(categoryId: Long, pageable: Pageable): Page<ProductResponse> {
        return productRepository.findByCategoryId(categoryId, pageable)
            .map { ProductResponse.fromEntity(it) }
    }
    
    fun updateProduct(id: Long, request: UpdateProductRequest): ProductResponse {
        val product = productRepository.findById(id)
            .filter { !it.isDeleted }
            .orElseThrow { throw RuntimeException("Product not found") }
        
        request.name?.let { product.name = it }
        request.description?.let { product.description = it }
        request.price?.let { product.price = it }
        request.quantity?.let { product.quantity = it }
        request.imageUrl?.let { product.imageUrl = it }
        request.categoryId?.let { 
            product.category = categoryRepository.findById(it)
                .orElseThrow { throw RuntimeException("Category not found") }
        }
        
        product.updatedAt = LocalDateTime.now()
        val updatedProduct = productRepository.save(product)
        
        return ProductResponse.fromEntity(updatedProduct)
    }
    
    fun deleteProduct(id: Long) {
        val product = productRepository.findById(id)
            .orElseThrow { throw RuntimeException("Product not found") }
        
        product.isDeleted = true
        productRepository.save(product)
    }
    
    fun updateProductStock(id: Long, quantity: Int) {
        val product = productRepository.findById(id)
            .orElseThrow { throw RuntimeException("Product not found") }
        
        product.quantity = quantity
        product.updatedAt = LocalDateTime.now()
        productRepository.save(product)
    }
    
    fun increaseProductRating(id: Long, rating: Double) {
        val product = productRepository.findById(id)
            .orElseThrow { throw RuntimeException("Product not found") }
        
        product.rating = rating
        product.reviewCount++
        product.updatedAt = LocalDateTime.now()
        productRepository.save(product)
    }
}
