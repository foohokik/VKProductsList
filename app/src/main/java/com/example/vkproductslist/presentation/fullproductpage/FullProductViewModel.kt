package com.example.vkproductslist.presentation.fullproductpage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vkproductslist.domain.model.ProductUI
import com.example.vkproductslist.presentation.fullproductpage.FullProductFragment.Companion.ARG_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class FullProductViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

  private val _contentScreen = MutableStateFlow(ProductUI.Product())
  val contentScreen = _contentScreen.asStateFlow()

  private var product: ProductUI.Product? = null

  init {
    product = savedStateHandle[ARG_ID]
    setContentScreen()
  }

  private fun setContentScreen() {
    if (product == null) {
      _contentScreen.value =
          contentScreen.value.copy(
              title = "",
              description = "Продукт не виден",
              price = 0,
              thumbnail = "",
              id = 0,
              category = "")
    } else {
      product?.let { _contentScreen.value = it }
    }
  }
}
