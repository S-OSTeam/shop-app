package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.apollo.AuthApolloService
import com.example.deamhome.data.model.response.Token

class NetworkAuthDataSource(
    private val apolloService: AuthApolloService,
) {
    suspend fun refreshToken(token: Token): Token {
        return apolloService.refreshToken(token)
    }
}
