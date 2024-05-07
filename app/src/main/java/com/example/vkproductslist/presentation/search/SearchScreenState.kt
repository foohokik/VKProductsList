package com.example.vkproductslist.presentation.search

import com.example.vkproductslist.domain.model.Products

data class SearchScreenState(
    var product: Products = Products(),
    var query: String ="",
    val keyboardState: Boolean = true
)
