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

class LoggingApolloInterceptor : ApolloInterceptor {
    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain,
    ): Flow<ApolloResponse<D>> {
        return chain.proceed(request).onEach { response ->
            log("$HTTP_LOG_TAG - ${request.operation.name()}: ${response.data}", LogLevel.E)
        }
    }

    companion object {
        private const val HTTP_LOG_TAG = "HTTP_LOG"
    }
}
