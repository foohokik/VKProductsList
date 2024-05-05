package com.example.vkproductslist.data.repository

import com.example.vkproductslist.core.network.NetworkResult
import com.example.vkproductslist.data.api.ProductsAPI
import com.example.vkproductslist.data.modelresponse.toNetworkResultProducts
import com.example.vkproductslist.domain.ProductsRepository
import com.example.vkproductslist.domain.model.Products
import javax.inject.Inject

class ProductsRepoImpl @Inject constructor(private val api: ProductsAPI) : ProductsRepository {

    override suspend fun getProducts(limit: Int, skip: Int): NetworkResult<Products> {
        val result = api.getProducts(limit, skip)
        return result.toNetworkResultProducts()
    }

}