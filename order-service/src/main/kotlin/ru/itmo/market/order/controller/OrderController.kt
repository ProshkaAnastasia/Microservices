package ru.itmo.market.order.controller

import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.market.model.dto.response.ApiResponse
import ru.itmo.market.model.dto.response.PaginatedResponse
import ru.itmo.market.order.dto.request.CreateOrderRequest
import ru.itmo.market.order.dto.request.UpdateOrderRequest
import ru.itmo.market.order.dto.response.OrderResponse
import ru.itmo.market.order.entity.OrderStatus
import ru.itmo.market.order.service.OrderService

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = ["*"])
class OrderController(
    private val orderService: OrderService
) {
    
    @PostMapping
    fun createOrder(@Valid @RequestBody request: CreateOrderRequest): ResponseEntity<ApiResponse<OrderResponse>> {
        val response = orderService.createOrder(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
    
    @PostMapping("/async")
    fun createOrderAsync(@Valid @RequestBody request: CreateOrderRequest): Mono<ApiResponse<OrderResponse>> {
        return orderService.createOrderAsync(request)
    }
    
    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): ResponseEntity<ApiResponse<OrderResponse>> {
        val response = orderService.getOrderById(id)
        return ResponseEntity.ok(response)
    }
    
    @GetMapping("/{id}/async")
    fun getOrderByIdAsync(@PathVariable id: Long): Mono<ApiResponse<OrderResponse>> {
        return orderService.getOrderByIdAsync(id)
    }
    
    @GetMapping
    fun getAllOrders(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int
    ): ResponseEntity<ApiResponse<PaginatedResponse<OrderResponse>>> {
        val pageable = PageRequest.of(page - 1, pageSize)
        val orders = orderService.getAllOrders(pageable)
        val paginatedResponse = PaginatedResponse.of(
            items = orders.content,
            page = page,
            pageSize = pageSize,
            totalItems = orders.totalElements
        )
        return ResponseEntity.ok(ApiResponse.success(paginatedResponse))
    }
    
    @GetMapping("/async")
    fun getAllOrdersAsync(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int
    ): Flux<OrderResponse> {
        val pageable = PageRequest.of(page - 1, pageSize)
        return orderService.getAllOrdersAsync(pageable)
    }
    
    @GetMapping("/user/{userId}")
    fun getOrdersByUserId(
        @PathVariable userId: Long,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int
    ): ResponseEntity<ApiResponse<PaginatedResponse<OrderResponse>>> {
        val pageable = PageRequest.of(page - 1, pageSize)
        val orders = orderService.getOrdersByUserId(userId, pageable)
        val paginatedResponse = PaginatedResponse.of(
            items = orders.content,
            page = page,
            pageSize = pageSize,
            totalItems = orders.totalElements
        )
        return ResponseEntity.ok(ApiResponse.success(paginatedResponse))
    }
    
    @GetMapping("/user/{userId}/async")
    fun getOrdersByUserIdAsync(
        @PathVariable userId: Long,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int
    ): Flux<OrderResponse> {
        val pageable = PageRequest.of(page - 1, pageSize)
        return orderService.getOrdersByUserIdAsync(userId, pageable)
    }
    
    @GetMapping("/status/{status}")
    fun getOrdersByStatus(
        @PathVariable status: OrderStatus,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int
    ): ResponseEntity<ApiResponse<PaginatedResponse<OrderResponse>>> {
        val pageable = PageRequest.of(page - 1, pageSize)
        val orders = orderService.getOrdersByStatus(status, pageable)
        val paginatedResponse = PaginatedResponse.of(
            items = orders.content,
            page = page,
            pageSize = pageSize,
            totalItems = orders.totalElements
        )
        return ResponseEntity.ok(ApiResponse.success(paginatedResponse))
    }
    
    @GetMapping("/status/{status}/async")
    fun getOrdersByStatusAsync(
        @PathVariable status: OrderStatus,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int
    ): Flux<OrderResponse> {
        val pageable = PageRequest.of(page - 1, pageSize)
        return orderService.getOrdersByStatusAsync(status, pageable)
    }
    
    @PutMapping("/{id}")
    fun updateOrder(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateOrderRequest
    ): ResponseEntity<ApiResponse<OrderResponse>> {
        val response = orderService.updateOrder(id, request)
        return ResponseEntity.ok(response)
    }
    
    @PutMapping("/{id}/async")
    fun updateOrderAsync(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateOrderRequest
    ): Mono<ApiResponse<OrderResponse>> {
        return orderService.updateOrderAsync(id, request)
    }
    
    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<ApiResponse<Map<String, String>>> {
        val response = orderService.deleteOrder(id)
        return ResponseEntity.ok(response)
    }
}
