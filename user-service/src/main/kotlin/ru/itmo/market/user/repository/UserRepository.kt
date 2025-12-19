package ru.itmo.market.user.repository

import ru.itmo.market.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    
    fun findByEmailAndIsDeletedFalse(email: String): Optional<User>
    
    fun findByUsernameAndIsDeletedFalse(username: String): Optional<User>
    
    @Query("SELECT u FROM User u WHERE u.isDeleted = false ORDER BY u.createdAt DESC")
    fun findAllActive(pageable: Pageable): Page<User>
    
    @Query("SELECT u FROM User u WHERE u.isDeleted = false AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    fun findBySearchTerm(@Param("search") search: String, pageable: Pageable): Page<User>
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isDeleted = false")
    fun countAllActive(): Long
}
