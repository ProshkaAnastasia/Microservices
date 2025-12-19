package ru.itmo.market.gateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class ResponseLoggingFilter : GlobalFilter {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        return chain.filter(exchange).then(
            Mono.fromRunnable {
                val response = exchange.response
                val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME)
                
                println("""
                    ╔════════════════════════════════════════════════════════════╗
                    ║ [RESPONSE] $timestamp
                    ║ Status: ${response.statusCode} | Path: ${exchange.request.path}
                    ╚════════════════════════════════════════════════════════════╝
                """.trimIndent())
            }
        )
    }
}
