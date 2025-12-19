package ru.itmo.market.product.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import ru.itmo.market.product.dto.request.CreateCategoryRequest
import ru.itmo.market.product.dto.response.CategoryResponse
import ru.itmo.market.product.service.CategoryService

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = ["*"])
class CategoryController(
    private val categoryService: CategoryService
) {
    
    @PostMapping
    fun createCategory(@Valid @RequestBody request: CreateCategoryRequest): ResponseEntity<CategoryResponse> {
        val category = categoryService.createCategory(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(category)
    }
    
    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<CategoryResponse> {
        val category = categoryService.getCategoryById(id)
        return ResponseEntity.ok(category)
    }
    
    @GetMapping
    fun getAllCategories(): Flux<CategoryResponse> {
        return categoryService.getAllCategories()
    }
    
    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateCategoryRequest
    ): ResponseEntity<CategoryResponse> {
        val category = categoryService.updateCategory(id, request)
        return ResponseEntity.ok(category)
    }
    
    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        categoryService.deleteCategory(id)
        return ResponseEntity.ok(mapOf("message" to "Category deleted successfully"))
    }
}
