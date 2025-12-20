package ru.itmo.market.order.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.market.order.entity.OrderItem

@Repository
interface OrderItemRepository : JpaRepository<OrderItem, Long> {
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.isDeleted = false")
    fun findByOrderId(@Param("orderId") orderId: Long): List<OrderItem>
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id IN :orderIds AND oi.isDeleted = false")
    fun findByOrderIds(@Param("orderIds") orderIds: List<Long>): List<OrderItem>
}
