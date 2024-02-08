package com.example.deamhome.data.apollo

import com.apollographql.apollo3.ApolloClient
import com.example.deamhome.data.model.response.Token
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.model.LoginMutation
import com.example.deamhome.model.SignUpMutation
import com.example.deamhome.model.type.LoginRequest
import com.example.deamhome.model.type.SignUpRequest

class AuthApolloService(private val apolloClient: ApolloClient) {
    suspend fun refreshToken(token: Token): Token {
        return Token("tmp", "tmp")
    }

    suspend fun signUp(signUpRequest: SignUpRequest): ApiResponse<String> {
        return apolloClient.executeMutation(SignUpMutation(signUpRequest)) { data ->
            data?.signUp
        }
    }

    suspend fun login(loginRequest: LoginRequest): ApiResponse<Token> {
        return apolloClient.executeMutation(LoginMutation(loginRequest)) { data ->
            data?.let {
                Token(it.login?.accessToken ?: "", it.login?.refreshToken ?: "")
            } ?: Token.EMPTY_TOKEN
        }
    }
}
