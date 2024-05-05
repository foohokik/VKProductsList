package com.example.vkproductslist.domain

import com.example.vkproductslist.core.network.NetworkResult
import com.example.vkproductslist.domain.model.Products

interface ProductsRepository {

    suspend fun getProducts (limit:Int, skip:Int): NetworkResult<Products>
    suspend fun searchProducts (searchQuery: String): NetworkResult<Products>


}