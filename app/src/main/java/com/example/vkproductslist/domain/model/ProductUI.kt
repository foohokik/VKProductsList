package com.example.vkproductslist.domain.model

import java.io.Serializable

sealed class ProductUI {

    data class Product(
        val category: String,
        val description: String,
        val id: Int,
        val price: Int,
        val rating: Double,
        val thumbnail: String,
        val title: String
    ) : Serializable, ProductUI()

    object Loading : ProductUI()
}
