package ru.itmo.market.gateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class RequestLoggingFilter : GlobalFilter {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request = exchange.request
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME)
        
        println("""
            ╔════════════════════════════════════════════════════════════╗
            ║ [REQUEST] $timestamp
            ║ Method: ${request.method} | Path: ${request.path}
            ║ Host: ${request.remoteAddress?.address?.hostAddress ?: "unknown"}
            ╚════════════════════════════════════════════════════════════╝
        """.trimIndent())
        
        return chain.filter(exchange)
    }
}