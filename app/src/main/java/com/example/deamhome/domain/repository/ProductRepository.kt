package com.example.deamhome.domain.repository

import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.Test

interface ProductRepository {
    suspend fun test(): ApiResponse<Test>
}
