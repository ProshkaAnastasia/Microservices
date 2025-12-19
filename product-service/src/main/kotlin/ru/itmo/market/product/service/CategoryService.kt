package ru.itmo.market.product.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import ru.itmo.market.product.dto.request.CreateCategoryRequest
import ru.itmo.market.product.dto.response.CategoryResponse
import ru.itmo.market.product.entity.Category
import ru.itmo.market.product.repository.CategoryRepository
import java.time.LocalDateTime

@Service
@Transactional
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    
    fun createCategory(request: CreateCategoryRequest): CategoryResponse {
        val existingCategory = categoryRepository.findByNameAndIsDeletedFalse(request.name)
        if (existingCategory.isPresent) {
            throw IllegalArgumentException("Category with name '${request.name}' already exists")
        }
        
        val category = Category(
            name = request.name,
            description = request.description,
            imageUrl = request.imageUrl
        )
        
        val savedCategory = categoryRepository.save(category)
        return CategoryResponse.fromEntity(savedCategory)
    }
    
    @Transactional(readOnly = true)
    fun getCategoryById(id: Long): CategoryResponse {
        val category = categoryRepository.findById(id)
            .filter { !it.isDeleted }
            .orElseThrow { throw RuntimeException("Category not found") }
        
        return CategoryResponse.fromEntity(category)
    }
    
    @Transactional(readOnly = true)
    fun getAllCategories(): Flux<CategoryResponse> {
        return Flux.fromIterable(categoryRepository.findAllByIsDeletedFalse())
            .subscribeOn(Schedulers.boundedElastic())
            .map { CategoryResponse.fromEntity(it) }
    }
    
    fun updateCategory(id: Long, request: CreateCategoryRequest): CategoryResponse {
        val category = categoryRepository.findById(id)
            .filter { !it.isDeleted }
            .orElseThrow { throw RuntimeException("Category not found") }
        
        category.name = request.name
        category.description = request.description
        category.imageUrl = request.imageUrl
        category.updatedAt = LocalDateTime.now()
        
        val updatedCategory = categoryRepository.save(category)
        return CategoryResponse.fromEntity(updatedCategory)
    }
    
    fun deleteCategory(id: Long) {
        val category = categoryRepository.findById(id)
            .orElseThrow { throw RuntimeException("Category not found") }
        
        category.isDeleted = true
        categoryRepository.save(category)
    }
}
