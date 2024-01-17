package com.example.deamhome.data.apollo

import com.apollographql.apollo3.ApolloClient
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.Test
import com.example.deamhome.model.TestMutation

class ProductApolloService(private val apolloClient: ApolloClient) {
    suspend fun getTest(): ApiResponse<Test> {
        return apolloClient.executeMutation(TestMutation()) {
            Test(it?.test ?: "")
        }
    }
}
