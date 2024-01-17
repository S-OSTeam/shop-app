package com.example.deamhome.data.apollo.interceptor

import android.util.Log
import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class LoggingApolloInterceptor : ApolloInterceptor {
    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain,
    ): Flow<ApolloResponse<D>> {
        return chain.proceed(request).onEach { response ->
            Log.e(
                HTTP_LOG_TAG,
                "Received response for ${request.operation.name()}: ${response.data}",
            )
        }
    }

    companion object {
        private const val HTTP_LOG_TAG = "HTTP_LOG"
    }
}
