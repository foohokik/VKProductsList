package com.example.vkproductslist.domain.model


data class Products(
    val limit: Int = 0,
    val products: List<ProductUI> = emptyList(),
    val skip: Int = 0,
    val total: Int = 0
)
