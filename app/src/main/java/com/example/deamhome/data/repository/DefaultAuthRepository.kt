package com.example.deamhome.data.repository

import com.apollographql.apollo3.api.Optional
import com.example.deamhome.data.datasource.local.LocalAuthDataSource
import com.example.deamhome.data.datasource.network.NetworkAuthDataSource
import com.example.deamhome.data.model.response.Token
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.AuthRepository
import com.example.deamhome.model.type.LoginRequest
import com.example.deamhome.model.type.SNS
import com.example.deamhome.model.type.SignUpRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DefaultAuthRepository(
    private val localAuthDataSource: LocalAuthDataSource,
    private val networkAuthDataSource: NetworkAuthDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {
    override val isLogin = localAuthDataSource.isLogin
    override suspend fun login(id: String, pwd: String, type: SNS): ApiResponse<Token> {
        return withContext(dispatcher) {
            val response = networkAuthDataSource.login(
                LoginRequest(
                    pwd = pwd,
                    userId = Optional.present(id),
                    email = Optional.present(""),
                    snsId = Optional.present(""),
                    sns = type,
                ),
            )

            if (response is ApiResponse.Success) {
                response.body?.let {
                    localAuthDataSource.updateToken(response.body)
                }
            }

            response
        }
    }

    // 토큰을 내부 저장소에서 가져오는
    override suspend fun getToken(): Token {
        return withContext(dispatcher) { localAuthDataSource.token.first() }
    }

    // 토큰을 갱신하는데 사용
    override suspend fun refreshToken() {
        withContext(dispatcher) {
            val token =
                networkAuthDataSource.refreshToken(getToken()) // 여기도 나중에 ApiResponse가 Success인 경우만 update하도록 수정해야함.
            localAuthDataSource.updateToken(token)
        }
    }

    override suspend fun removeToken() {
        withContext(dispatcher) {
            localAuthDataSource.removeToken()
        }
    }

    override suspend fun updateToken(token: Token) {
        withContext(dispatcher) {
            localAuthDataSource.updateToken(token)
        }
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): ApiResponse<String> {
        return withContext(dispatcher) {
            networkAuthDataSource.signUp(signUpRequest)
        }
    }
}
