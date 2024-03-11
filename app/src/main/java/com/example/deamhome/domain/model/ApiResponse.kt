package com.example.deamhome.domain.model

import com.apollographql.apollo3.api.Error

sealed class ApiResponse<out T : Any> {
    data class Success<T : Any>(val body: T?) : ApiResponse<T>()
    data class Failure<T : Any>(val body: T?, val errors: List<Error>?) : ApiResponse<T>()
    data class HttpFailure(val responseCode: Int, val error: String?) : ApiResponse<Nothing>()
    data class NetworkError(val message: String?) : ApiResponse<Nothing>()
}
