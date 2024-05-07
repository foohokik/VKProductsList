package com.example.vkproductslist.presentation.pagination

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OnScrollListener(
    private val paginationListener: PaginationListener,
    private val layoutManager: LinearLayoutManager
): RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        if (!paginationListener.isLoading()) {
            when {
                lastVisibleItemPosition + PRELOAD_ITEM_COUNT >= totalItemCount - 1 &&
                        !paginationListener.isLastItems() &&
                        lastVisibleItemPosition > 0 -> {
                    paginationListener.loadNextItems()
                }
            }
        }
    }

    companion object {
        private const val PRELOAD_ITEM_COUNT = 1
    }
}
