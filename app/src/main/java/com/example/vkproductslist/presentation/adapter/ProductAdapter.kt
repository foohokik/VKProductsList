package com.example.vkproductslist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vkproductslist.databinding.ItemProductBinding
import com.example.vkproductslist.databinding.LoadingItemBinding
import com.example.vkproductslist.domain.model.ProductUI

class ProductAdapter(private val listener: ProductListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val items = mutableListOf<ProductUI>()

  override fun getItemViewType(position: Int) =
      when (items[position]) {
        is ProductUI.Product -> TYPE_ITEM
        is ProductUI.Loading -> TYPE_LOADING
        else -> throw IllegalArgumentException("Invalid type of item $position")
      }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)

    return when (viewType) {
      TYPE_ITEM -> ArticleViewHolder(ItemProductBinding.inflate(layoutInflater, parent, false))
      TYPE_LOADING -> LoadingViewHolder(LoadingItemBinding.inflate(layoutInflater, parent, false))
      else -> LoadingViewHolder(LoadingItemBinding.inflate(layoutInflater, parent, false))
    }
  }

  override fun getItemCount(): Int = items.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is ArticleViewHolder -> holder.bind(items[position] as ProductUI.Product, listener)
      is LoadingViewHolder -> holder.bind(items[position] as ProductUI.Loading)
    }
  }

  fun setItems(newItems: List<ProductUI>) {
    val diffResult = DiffUtil.calculateDiff(ProductDiffUtil(items, newItems))
    items.clear()
    items.addAll(newItems)
    diffResult.dispatchUpdatesTo(this)
  }

  companion object {
    private const val TYPE_ITEM = 0
    private const val TYPE_LOADING = 1
  }
}
