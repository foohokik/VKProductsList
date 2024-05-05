package com.example.vkproductslist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ProductUI {
  @Parcelize
  data class Product(
      val category: String = "",
      val description: String = "",
      val id: Int = 0,
      val price: Int = 0,
      val thumbnail: String = "",
      val title: String = ""
  ) : ProductUI(), Parcelable

  object Loading : ProductUI()
}
