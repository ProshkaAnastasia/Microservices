package ru.itmo.market.gateway.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
@Order(-1)
class GlobalErrorHandler(
    private val objectMapper: ObjectMapper
) : ErrorWebExceptionHandler {

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val status = when (ex) {
            is ResponseStatusException -> HttpStatus.NOT_FOUND
            is IllegalArgumentException -> HttpStatus.BAD_REQUEST
            is IllegalStateException -> HttpStatus.INTERNAL_SERVER_ERROR
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        val errorResponse = mapOf(
            "timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            "status" to status.value(),
            "error" to status.reasonPhrase,
            "message" to (ex.message ?: "Unknown error"),
            "path" to exchange.request.path.value()
        )

        exchange.response.statusCode = status
        exchange.response.headers.contentType = MediaType.APPLICATION_JSON

        return exchange.response.writeWith(
            Mono.fromCallable {
                exchange.response.bufferFactory().wrap(
                    objectMapper.writeValueAsBytes(errorResponse)
                )
            }
        )
    }
}
