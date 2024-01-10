package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.apollo.ProductApolloService
import com.example.deamhome.data.retrofit.ProductRetrofitService

class NetworkProductDataSource(
    private val retrofitService: ProductRetrofitService,
    private val apolloService: ProductApolloService,
)
