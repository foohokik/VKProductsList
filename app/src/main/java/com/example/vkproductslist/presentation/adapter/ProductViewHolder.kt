package com.example.vkproductslist.presentation.adapter


import android.provider.Settings.System.getString
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkproductslist.R
import com.example.vkproductslist.databinding.ItemProductBinding
import com.example.vkproductslist.domain.model.ProductUI

class ArticleViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

  fun bind(product: ProductUI.Product, listener: ProductListener) =
      with(binding) {
        Glide.with(ivThumbnail.context).load(product.thumbnail).circleCrop().into(ivThumbnail)
        tvTitle.text = product.title
        tvDescription.text = product.description
        val price = product.price.toString() + "$"
        tvPrice.text = price

        root.setOnClickListener { listener.onClickArticle(product) }
      }
}
