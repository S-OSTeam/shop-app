package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.apollo.ProductApolloService
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.Test

class NetworkProductDataSource(
    private val productService: ProductApolloService,
) {
    suspend fun test(): ApiResponse<Test> {
        return ApiResponse.Success(Test("dafs"))
    }
}
