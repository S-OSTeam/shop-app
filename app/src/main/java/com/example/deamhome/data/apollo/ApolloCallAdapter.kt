package com.example.deamhome.data.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.example.deamhome.common.util.LogLevel
import com.example.deamhome.common.util.log
import com.example.deamhome.common.util.reportNonSeriousException
import com.example.deamhome.domain.model.ApiResponse

// 요청하려는 Mutation이나 Query를 sealed class로 감싸서 뱉도록 만드는 녀석
suspend fun <T : Mutation.Data, R : Any> ApolloClient.executeMutation(
    mutation: Mutation<T>,
    map: (T?) -> R?,
): ApiResponse<R> {
    try {
        val response = this.mutation(mutation).execute()
        if (response.hasErrors()) {
            log("Failed with no exception: ${response.errors?.joinToString()}", LogLevel.W)
            return ApiResponse.Unexpected(null)
        }
        val data = response.data
        return ApiResponse.Success(map(data))
    } catch (e: ApolloException) {
        // 일단은 크래시리틱스에 심각하지 않은 예외 수준으로 올리기
        reportNonSeriousException(e) {
            key("mutationName", mutation.name())
        }
        return when (e) {
            is ApolloNetworkException -> ApiResponse.NetworkError(e.message)
            is ApolloHttpException -> ApiResponse.Failure(e.statusCode, e.message)
            else -> ApiResponse.Unexpected(e.cause)
        }
    }
}

suspend fun <T : Query.Data, R : Any> ApolloClient.executeQuery(
    query: Query<T>,
    map: (T?) -> R?,
): ApiResponse<R> {
    try {
        val response = this.query(query).execute()
        if (response.hasErrors()) {
            log("Failed with no exception: ${response.errors?.get(0)?.message}", LogLevel.W)
            return ApiResponse.Unexpected(null)
        }
        val data = response.data
        return ApiResponse.Success(map(data))
    } catch (e: ApolloException) {
        // 일단은 크래시리틱스에 심각하지 않은 예외 수준으로 올리기
        reportNonSeriousException(e) {
            key("queryName", query.name())
        }
        return when (e) {
            is ApolloNetworkException -> ApiResponse.NetworkError(e.message)
            is ApolloHttpException -> ApiResponse.Failure(e.statusCode, e.message)
            else -> ApiResponse.Unexpected(e.cause)
        }
    }
}
