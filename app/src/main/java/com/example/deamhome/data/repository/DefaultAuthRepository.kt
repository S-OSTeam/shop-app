package com.example.deamhome.data.repository

import com.example.deamhome.data.datasource.local.LocalAuthDataSource
import com.example.deamhome.data.datasource.network.NetworkAuthDataSource
import com.example.deamhome.data.model.Token
import com.example.deamhome.domain.repository.AuthRepository
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

    // 토큰을 내부 저장소에서 가져오는
    override suspend fun getToken(): Token {
        return withContext(dispatcher) { localAuthDataSource.token.first() }
    }

    // 토큰을 갱신하는데 사용
    override suspend fun refreshToken() {
        withContext(dispatcher) {
            val token = networkAuthDataSource.refreshToken(getToken()) // 여기도 나중에 ApiResponse가 Success인 경우만 update하도록 수정해야함.
            localAuthDataSource.updateToken(token)
        }
    }

    override suspend fun removeToken() {
        withContext(dispatcher) {
            localAuthDataSource.removeToken()
        }
    }
}
