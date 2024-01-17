package com.example.deamhome.data.apollo.interceptor

import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.example.deamhome.domain.repository.AuthRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthorizationInterceptor(
    private val authRepository: AuthRepository,
) : HttpInterceptor {
    private val mutex = Mutex()

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain,
    ): HttpResponse {
        var token = mutex.withLock {
            authRepository.getToken().accessToken
        }

        val response =
            chain.proceed(
                request.newBuilder().addHeader(AUTHORIZATION, "$TOKEN_PREFIX $token").build(),
            )

        return if (response.statusCode == 401) {
            token = mutex.withLock {
                authRepository.refreshToken()
                authRepository.getToken().accessToken
            }
            val response2 = chain.proceed(
                request.newBuilder().addHeader(AUTHORIZATION, "$TOKEN_PREFIX $token").build(),
            )
            if (response2.statusCode == 401) { // 갱신했는데도 토큰 만료인 경우
                mutex.withLock { authRepository.removeToken() } // 토큰을 지움. => 비로그인 상태로 전환하겠다는 의미.
            }
            response2
        } else {
            response
        }
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val TOKEN_PREFIX = "DBearer"
    }
}
