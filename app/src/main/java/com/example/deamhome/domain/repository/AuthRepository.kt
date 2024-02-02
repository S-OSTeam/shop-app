package com.example.deamhome.domain.repository

import com.example.deamhome.data.model.response.Token
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.model.type.SignUpRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLogin: Flow<Boolean>
    suspend fun getToken(): Token
    suspend fun refreshToken()
    suspend fun removeToken()
    suspend fun updateToken(token: Token)
    suspend fun signUp(signUpRequest: SignUpRequest): ApiResponse<String>
}
