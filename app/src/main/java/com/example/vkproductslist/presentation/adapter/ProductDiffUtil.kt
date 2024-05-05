package com.example.vkproductslist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.vkproductslist.domain.model.ProductUI

class ProductDiffUtil(
    private val oldList: MutableList<ProductUI>,
    private val newList: List<ProductUI>
): DiffUtil.Callback()  {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is  ProductUI.Product && newItem is ProductUI.Product) {
            oldItem.id == newItem.id
        } else {
            oldItem.javaClass == newItem.javaClass
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}