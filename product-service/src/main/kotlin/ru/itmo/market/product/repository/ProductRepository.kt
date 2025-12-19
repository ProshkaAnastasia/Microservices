package ru.itmo.market.product.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.market.product.entity.Product
import java.util.Optional

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    
    fun findByNameAndIsDeletedFalse(name: String): Optional<Product>
    
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false ORDER BY p.createdAt DESC")
    fun findAllActive(pageable: Pageable): Page<Product>
    
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.isDeleted = false")
    fun findByCategoryId(@Param("categoryId") categoryId: Long, pageable: Pageable): Page<Product>
    
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    fun findBySearchTerm(@Param("search") search: String, pageable: Pageable): Page<Product>
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.isDeleted = false AND p.category.id = :categoryId")
    fun countActiveByCategory(@Param("categoryId") categoryId: Long): Long
}
