package com.example.deamhome.data.apollo

import com.apollographql.apollo3.ApolloClient
import com.example.deamhome.data.model.response.Token

class AuthApolloService(private val apolloClient: ApolloClient) {
    suspend fun refreshToken(token: Token): Token {
        return Token("tmp", "tmp")
    }
}
