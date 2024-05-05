package com.example.vkproductslist.presentation.adapter

import com.example.vkproductslist.domain.model.ProductUI


interface ProductListener {
    fun onClickArticle (product: ProductUI.Product)
}