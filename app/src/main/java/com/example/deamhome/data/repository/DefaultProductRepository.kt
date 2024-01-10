package com.example.deamhome.data.repository

import com.example.deamhome.data.datasource.network.NetworkProductDataSource
import com.example.deamhome.domain.ProductRepository

class DefaultProductRepository(
    private val networkProductDataSource: NetworkProductDataSource,
) : ProductRepository
