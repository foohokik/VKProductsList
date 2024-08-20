package com.example.vkproductslist.presentation.fullproductpage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vkproductslist.domain.model.ProductUI
import com.example.vkproductslist.presentation.fullproductpage.FullProductFragment.Companion.ARG_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
      _contentScreen.update { state ->
          product
              ?: state.copy(
                  title = "",
                  description = "",
                  price = 0.0,
                  thumbnail = "",
                  id = 0,
                  category = "")
      }
    }
  }

