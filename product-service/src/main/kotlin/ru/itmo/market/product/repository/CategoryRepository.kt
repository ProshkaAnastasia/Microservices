package ru.itmo.market.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.market.product.entity.Category
import java.util.Optional

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    
    fun findByNameAndIsDeletedFalse(name: String): Optional<Category>
    
    fun findAllByIsDeletedFalse(): List<Category>
}
