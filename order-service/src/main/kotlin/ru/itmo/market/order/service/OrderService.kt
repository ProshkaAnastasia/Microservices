package ru.itmo.market.order.service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.itmo.market.exception.ResourceNotFoundException
import ru.itmo.market.model.dto.response.ApiResponse
import ru.itmo.market.model.dto.response.PaginatedResponse
import ru.itmo.market.order.client.ProductServiceClient
import ru.itmo.market.order.client.UserServiceClient
import ru.itmo.market.order.dto.request.CreateOrderRequest
import ru.itmo.market.order.dto.request.UpdateOrderRequest
import ru.itmo.market.order.dto.response.OrderItemResponse
import ru.itmo.market.order.dto.response.OrderResponse
import ru.itmo.market.order.entity.Order
import ru.itmo.market.order.entity.OrderItem
import ru.itmo.market.order.entity.OrderStatus
import ru.itmo.market.order.repository.OrderItemRepository
import ru.itmo.market.order.repository.OrderRepository
import ru.itmo.market.util.DateUtils
import java.time.LocalDateTime

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val userServiceClient: UserServiceClient,
    private val productServiceClient: ProductServiceClient
) {
    
    private val logger = LoggerFactory.getLogger(this::class.java)
    
    fun createOrderAsync(request: CreateOrderRequest): Mono<ApiResponse<OrderResponse>> {
        return Mono.fromCallable {
            logger.info("Creating order for user: {}", request.userId)
            val user = userServiceClient.getUserById(request.userId)
                ?: throw ResourceNotFoundException(
                    message = "User not found",
                    resource = "User",
                    resourceId = request.userId
                )
            
            val order = Order(
                userId = request.userId,
                status = OrderStatus.PENDING,
                totalPrice = request.items.map { it.price.multiply(it.quantity.toBigDecimal()) }
                    .fold(java.math.BigDecimal.ZERO) { acc, price -> acc.add(price) },
                shippingAddress = request.shippingAddress,
                notes = request.notes
            )
            
            val savedOrder = orderRepository.save(order)
            val orderItems = request.items.map { item ->
                OrderItem(
                    order = savedOrder,
                    productId = item.productId,
                    quantity = item.quantity,
                    price = item.price
                )
            }
            
            orderItemRepository.saveAll(orderItems)
            
            val itemResponses = orderItems.map { oi ->
                OrderItemResponse(
                    id = oi.id,
                    productId = oi.productId,
                    quantity = oi.quantity,
                    price = oi.price
                )
            }
            
            logger.info("Order created successfully with id: {}", savedOrder.id)
            ApiResponse.success(
                data = OrderResponse.fromEntity(savedOrder, itemResponses),
                message = "Order created successfully"
            )
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnError { logger.error("Error creating order", it) }
    }
    
    fun createOrder(request: CreateOrderRequest): ApiResponse<OrderResponse> {
        logger.info("Creating order synchronously for user: {}", request.userId)
        
        val user = userServiceClient.getUserById(request.userId)
            ?: throw ResourceNotFoundException(
                message = "User not found",
                resource = "User",
                resourceId = request.userId
            )
        
        val order = Order(
            userId = request.userId,
            status = OrderStatus.PENDING,
            totalPrice = request.items.map { it.price.multiply(it.quantity.toBigDecimal()) }
                .fold(java.math.BigDecimal.ZERO) { acc, price -> acc.add(price) },
            shippingAddress = request.shippingAddress,
            notes = request.notes
        )
        
        val savedOrder = orderRepository.save(order)
        
        val orderItems = request.items.map { item ->
            OrderItem(
                order = savedOrder,
                productId = item.productId,
                quantity = item.quantity,
                price = item.price
            )
        }
        
        orderItemRepository.saveAll(orderItems)
        
        val itemResponses = orderItems.map { oi ->
            OrderItemResponse(
                id = oi.id,
                productId = oi.productId,
                quantity = oi.quantity,
                price = oi.price
            )
        }
        
        logger.info("Order created successfully with id: {}", savedOrder.id)
        return ApiResponse.success(
            data = OrderResponse.fromEntity(savedOrder, itemResponses),
            message = "Order created successfully"
        )
    }
    
    @Transactional(readOnly = true)
    fun getOrderByIdAsync(id: Long): Mono<ApiResponse<OrderResponse>> {
        return Mono.fromCallable {
            logger.debug("Fetching order asynchronously: {}", id)
            
            val order = orderRepository.findById(id)
                .filter { !it.isDeleted }
                .orElseThrow {
                    ResourceNotFoundException(
                        message = "Order not found",
                        resource = "Order",
                        resourceId = id
                    )
                }
            
            val items = orderItemRepository.findByOrderId(id)
                .map { oi ->
                    OrderItemResponse(
                        id = oi.id,
                        productId = oi.productId,
                        quantity = oi.quantity,
                        price = oi.price
                    )
                }
            
            ApiResponse.success(OrderResponse.fromEntity(order, items))
        }
        .subscribeOn(Schedulers.boundedElastic())
        .doOnError { logger.error("Error fetching order: {}", id, it) }
    }
    
    @Transactional(readOnly = true)
    fun getOrderById(id: Long): ApiResponse<OrderResponse> {
        logger.debug("Fetching order: {}", id)
        
        val order = orderRepository.findById(id)
            .filter { !it.isDeleted }
            .orElseThrow {
                ResourceNotFoundException(
                    message = "Order not found",
                    resource = "Order",
                    resourceId = id
                )
            }
        
        val items = orderItemRepository.findByOrderId(id)
            .map { oi ->
                OrderItemResponse(
                    id = oi.id,
                    productId = oi.productId,
                    quantity = oi.quantity,
                    price = oi.price
                )
            }
        
        return ApiResponse.success(OrderResponse.fromEntity(order, items))
    }
    
    @Transactional(readOnly = true)
    fun getAllOrdersAsync(pageable: Pageable): Flux<OrderResponse> {
        logger.debug("Fetching all orders asynchronously")
        
        return Flux.fromIterable(
            orderRepository.findAllActive(pageable).content
        )
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap { order ->
            val items = orderItemRepository.findByOrderId(order.id)
                .map { oi ->
                    OrderItemResponse(
                        id = oi.id,
                        productId = oi.productId,
                        quantity = oi.quantity,
                        price = oi.price
                    )
                }
            
            Mono.just(OrderResponse.fromEntity(order, items))
        }
    }
    
    @Transactional(readOnly = true)
    fun getAllOrders(pageable: Pageable): Page<OrderResponse> {
        logger.debug("Fetching all orders")
        
        val orders = orderRepository.findAllActive(pageable)
        val itemsByOrderId = orderItemRepository.findByOrderIds(orders.content.map { it.id })
            .groupBy { it.order?.id }
        
        return orders.map { order ->
            val items = itemsByOrderId[order.id]?.map { oi ->
                OrderItemResponse(
                    id = oi.id,
                    productId = oi.productId,
                    quantity = oi.quantity,
                    price = oi.price
                )
            } ?: emptyList()
            
            OrderResponse.fromEntity(order, items)
        }
    }
    
    @Transactional(readOnly = true)
    fun getOrdersByUserIdAsync(userId: Long, pageable: Pageable): Flux<OrderResponse> {
        logger.debug("Fetching orders for user asynchronously: {}", userId)
        
        return Flux.fromIterable(
            orderRepository.findByUserId(userId, pageable).content
        )
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap { order ->
            val items = orderItemRepository.findByOrderId(order.id)
                .map { oi ->
                    OrderItemResponse(
                        id = oi.id,
                        productId = oi.productId,
                        quantity = oi.quantity,
                        price = oi.price
                    )
                }
            
            Mono.just(OrderResponse.fromEntity(order, items))
        }
    }
    
    @Transactional(readOnly = true)
    fun getOrdersByUserId(userId: Long, pageable: Pageable): Page<OrderResponse> {
        logger.debug("Fetching orders for user: {}", userId)
        
        val orders = orderRepository.findByUserId(userId, pageable)
        val itemsByOrderId = orderItemRepository.findByOrderIds(orders.content.map { it.id })
            .groupBy { it.order?.id }
        
        return orders.map { order ->
            val items = itemsByOrderId[order.id]?.map { oi ->
                OrderItemResponse(
                    id = oi.id,
                    productId = oi.productId,
                    quantity = oi.quantity,
                    price = oi.price
                )
            } ?: emptyList()
            
            OrderResponse.fromEntity(order, items)
        }
    }
    
    @Transactional(readOnly = true)
    fun getOrdersByStatusAsync(status: OrderStatus, pageable: Pageable): Flux<OrderResponse> {
        logger.debug("Fetching orders by status asynchronously: {}", status)
        
        return Flux.fromIterable(
            orderRepository.findByStatus(status, pageable).content
        )
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap { order ->
            val items = orderItemRepository.findByOrderId(order.id)
                .map { oi ->
                    OrderItemResponse(
                        id = oi.id,
                        productId = oi.productId,
                        quantity = oi.quantity,
                        price = oi.price
                    )
                }
            
            Mono.just(OrderResponse.fromEntity(order, items))
        }
    }
    
    @Transactional(readOnly = true)
    fun getOrdersByStatus(status: OrderStatus, pageable: Pageable): Page<OrderResponse> {
        logger.debug("Fetching orders by status: {}", status)
        
        val orders = orderRepository.findByStatus(status, pageable)
        val itemsByOrderId = orderItemRepository.findByOrderIds(orders.content.map { it.id })
            .groupBy { it.order?.id }
        
        return orders.map { order ->
            val items = itemsByOrderId[order.id]?.map { oi ->
                OrderItemResponse(
                    id = oi.id,
                    productId = oi.productId,
                    quantity = oi.quantity,
                    price = oi.price
                )
            } ?: emptyList()
            
            OrderResponse.fromEntity(order, items)
        }
    }
    
    fun updateOrder(id: Long, request: UpdateOrderRequest): ApiResponse<OrderResponse> {
        logger.info("Updating order: {}", id)
        
        val order = orderRepository.findById(id)
            .filter { !it.isDeleted }
            .orElseThrow {
                ResourceNotFoundException(
                    message = "Order not found",
                    resource = "Order",
                    resourceId = id
                )
            }
        
        request.status?.let { order.status = it }
        request.shippingAddress?.let { order.shippingAddress = it }
        request.notes?.let { order.notes = it }
        
        order.updatedAt = DateUtils.now()
        val updatedOrder = orderRepository.save(order)
        
        val items = orderItemRepository.findByOrderId(id)
            .map { oi ->
                OrderItemResponse(
                    id = oi.id,
                    productId = oi.productId,
                    quantity = oi.quantity,
                    price = oi.price
                )
            }
        
        logger.info("Order updated successfully: {}", id)
        return ApiResponse.success(
            data = OrderResponse.fromEntity(updatedOrder, items),
            message = "Order updated successfully"
        )
    }
    
    fun updateOrderAsync(id: Long, request: UpdateOrderRequest): Mono<ApiResponse<OrderResponse>> {
        return Mono.fromCallable {
            updateOrder(id, request)
        }
        .subscribeOn(Schedulers.boundedElastic())
    }
    
    fun deleteOrder(id: Long): ApiResponse<Map<String, String>> {
        logger.info("Deleting order: {}", id)
        
        val order = orderRepository.findById(id)
            .orElseThrow {
                ResourceNotFoundException(
                    message = "Order not found",
                    resource = "Order",
                    resourceId = id
                )
            }
        
        order.isDeleted = true
        orderRepository.save(order)
        
        logger.info("Order deleted successfully: {}", id)
        return ApiResponse.success(
            data = mapOf("message" to "Order deleted successfully"),
            message = "Order deleted successfully"
        )
    }
}
