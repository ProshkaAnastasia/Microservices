package ru.itmo.market.order.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.market.order.entity.Order
import ru.itmo.market.order.entity.OrderStatus

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    
    @Query("SELECT o FROM Order o WHERE o.isDeleted = false ORDER BY o.createdAt DESC")
    fun findAllActive(pageable: Pageable): Page<Order>
    
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.isDeleted = false ORDER BY o.createdAt DESC")
    fun findByUserId(@Param("userId") userId: Long, pageable: Pageable): Page<Order>
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.isDeleted = false ORDER BY o.createdAt DESC")
    fun findByStatus(@Param("status") status: OrderStatus, pageable: Pageable): Page<Order>
    
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status = :status AND o.isDeleted = false")
    fun findByUserIdAndStatus(
        @Param("userId") userId: Long,
        @Param("status") status: OrderStatus,
        pageable: Pageable
    ): Page<Order>
}
