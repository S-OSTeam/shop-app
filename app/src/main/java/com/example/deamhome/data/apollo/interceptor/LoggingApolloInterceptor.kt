package com.example.deamhome.data.apollo.interceptor

import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import com.example.deamhome.common.util.LogLevel
import com.example.deamhome.common.util.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class LoggingApolloInterceptor(
    private val deviceId: String,
) : ApolloInterceptor {
    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain,
    ): Flow<ApolloResponse<D>> {
        return chain.proceed(
            request.newBuilder().addHttpHeader(AUTHORIZATION_MAC, "${deviceId}$MAC_SUFFIX").build(),
        ).onEach { response ->
            log(HTTP_LOG_TAG, "${request.operation.name()}: ${response.data}", LogLevel.E)
        }
    }

    companion object {
        private const val HTTP_LOG_TAG = "HTTP_LOG"

        private const val AUTHORIZATION_MAC = "Authorization-Mac"
        private const val MAC_SUFFIX = "M"
    }
}
