package ru.itmo.market.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import jakarta.servlet.http.HttpServletRequest

@RestControllerAdvice
class GlobalExceptionHandler(
    private val request: HttpServletRequest
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(
        exception: ResourceNotFoundException,
        webRequest: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Resource not found: ${exception.message}")
        val errorResponse = ErrorResponse.fromException(
            exception,
            path = request.requestURI,
            status = HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(
        exception: ConflictException,
        webRequest: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Conflict error: ${exception.message}")
        val errorResponse = ErrorResponse.fromException(
            exception,
            path = request.requestURI,
            status = HttpStatus.CONFLICT.value()
        )
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(
        exception: ForbiddenException,
        webRequest: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Forbidden error: ${exception.message}")
        val errorResponse = ErrorResponse.fromException(
            exception,
            path = request.requestURI,
            status = HttpStatus.FORBIDDEN.value()
        )
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(
        exception: ValidationException,
        webRequest: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Validation error: ${exception.message}")
        val errorResponse = ErrorResponse.fromException(
            exception,
            path = request.requestURI,
            status = HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException,
        webRequest: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errors = mutableMapOf<String, String>()
        exception.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "Invalid field"
            errors[fieldName] = errorMessage
        }
        
        logger.warn("Validation failed: $errors")
        val errorResponse = ErrorResponse(
            errorCode = "VALIDATION_ERROR",
            message = "Validation failed",
            path = request.requestURI,
            details = errors as Map<String, Any>,
            status = HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(
        exception: ApplicationException,
        webRequest: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("Application error: ${exception.message}", exception)
        val errorResponse = ErrorResponse.fromException(
            exception,
            path = request.requestURI,
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        exception: Exception,
        webRequest: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected error", exception)
        val errorResponse = ErrorResponse(
            errorCode = "INTERNAL_ERROR",
            message = "An unexpected error occurred",
            path = request.requestURI,
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
