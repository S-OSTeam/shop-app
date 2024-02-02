package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.apollo.AuthApolloService
import com.example.deamhome.data.model.response.Token
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.model.type.SignUpRequest

class NetworkAuthDataSource(
    private val apolloService: AuthApolloService,
) {
    suspend fun refreshToken(token: Token): Token {
        return apolloService.refreshToken(token)
    }

    suspend fun signUp(signUpRequest: SignUpRequest): ApiResponse<String> {
        return apolloService.signUp(signUpRequest)
    }
}
