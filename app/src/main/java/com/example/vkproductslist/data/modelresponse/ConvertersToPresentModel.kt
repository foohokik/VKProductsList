package com.example.vkproductslist.data.modelresponse

import com.example.vkproductslist.core.network.NetworkResult
import com.example.vkproductslist.domain.model.ProductUI
import com.example.vkproductslist.domain.model.Products

fun ProductResponse.toProduct (): ProductUI.Product {
    return ProductUI.Product (category, description, id, price, thumbnail, title)
}

fun List<ProductResponse>.toProducts(): List<ProductUI.Product> {
    return this.map { it.toProduct() }
}

fun ProductsResponse.toProducts (): Products {
    return Products(limit, products.toProducts(), skip, total)
}

fun NetworkResult<ProductsResponse>.toNetworkResultProducts(): NetworkResult<Products> {
    return when (this) {
        is NetworkResult.Success<ProductsResponse> -> {
            NetworkResult.Success(this.data.toProducts())
        }

        is NetworkResult.Error<ProductsResponse> -> {
            NetworkResult.Error(this.code, this.message)
        }

        is NetworkResult.Exception -> {
            NetworkResult.Exception(this.e)
        }
    }
}
