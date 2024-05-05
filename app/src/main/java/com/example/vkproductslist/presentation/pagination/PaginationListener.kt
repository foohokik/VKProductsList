package com.example.vkproductslist.presentation.pagination

interface PaginationListener {
  fun isLoading(): Boolean

  fun isLastItems(): Boolean

  fun loadNextItems(isError: Boolean = false)
}
