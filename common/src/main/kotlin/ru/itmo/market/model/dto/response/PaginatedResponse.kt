package ru.itmo.market.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PaginatedResponse<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
) {
    companion object {
        fun <T> of(
            items: List<T>,
            page: Int,
            pageSize: Int,
            totalItems: Long
        ): PaginatedResponse<T> {
            val totalPages = (totalItems + pageSize - 1) / pageSize
            return PaginatedResponse(
                items = items,
                page = page,
                pageSize = pageSize,
                totalItems = totalItems,
                totalPages = totalPages.toInt(),
                hasNextPage = page < totalPages,
                hasPreviousPage = page > 1
            )
        }
    }
}
