package com.example.vkproductslist.data.modelresponse

data class ProductsResponse(
    val limit: Int,
    val products: List<ProductResponse>,
    val skip: Int,
    val total: Int
)