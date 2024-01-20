package com.example.deamhome.domain.repository

import com.example.deamhome.data.model.Token
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLogin: Flow<Boolean>
    suspend fun getToken(): Token
    suspend fun refreshToken()
    suspend fun removeToken()
    suspend fun updateToken(token: Token)
}
