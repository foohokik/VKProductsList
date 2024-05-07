package com.example.vkproductslist.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkproductslist.core.network.onError
import com.example.vkproductslist.core.network.onException
import com.example.vkproductslist.core.network.onSuccess
import com.example.vkproductslist.di.IoDispatcher
import com.example.vkproductslist.domain.ProductsRepository
import com.example.vkproductslist.domain.model.ProductUI
import com.example.vkproductslist.domain.model.Products
import com.example.vkproductslist.presentation.SideEffects
import com.example.vkproductslist.presentation.adapter.ProductListener
import com.example.vkproductslist.presentation.pagination.PaginationListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProductsViewModel
@Inject
constructor(
    private val repository: ProductsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), ProductListener, PaginationListener {

  private val _productsFlow = MutableStateFlow(Products())
  val productsFlow = _productsFlow.asStateFlow()

  private val _stateProgressBar = MutableStateFlow(true)
  val stateProgressBar = _stateProgressBar.asStateFlow()

  private val _sideEffects = Channel<SideEffects>()
  val sideEffects = _sideEffects.receiveAsFlow()

  private var skip = 0
  private var isLoading = false

  private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    viewModelScope.launch { _sideEffects.send(SideEffects.ExceptionEffect(throwable)) }
  }

  init {
    getProducts()
  }

  private fun getProducts() {
    viewModelScope.launch(exceptionHandler) {
      withContext(ioDispatcher) {
        isLoading = true
        if (skip >= 20) {
          _productsFlow.update {
            it.copy(products = productsFlow.value.products + ProductUI.Loading)
          }
        } else {
          _stateProgressBar.value = isLoading
        }
        val result = repository.getProducts(LIMIT, skip)
        result
            .onSuccess { products ->
              _productsFlow.value =
                  products.copy(products = productsFlow.value.products + products.products)
              if (skip < products.total) {
                skip += LIMIT
              }
            }
            .onError { _, message -> _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty())) }
            .onException { throwable -> _sideEffects.send(SideEffects.ExceptionEffect(throwable)) }

        _productsFlow.update {
          it.copy(products = productsFlow.value.products.filterIsInstance<ProductUI.Product>())
        }

        isLoading = false
        _stateProgressBar.value = isLoading
      }
    }
  }

  override fun onClickArticle(product: ProductUI.Product) {
    viewModelScope.launch { _sideEffects.send(SideEffects.ClickEffect(product)) }
  }

  override fun isLoading(): Boolean {
    return isLoading
  }

  override fun isLastItems(): Boolean {
    return _productsFlow.value.total <= skip
  }

  override fun loadNextItems(isError: Boolean) {
    getProducts()
  }

  companion object {
    private const val LIMIT = 20
  }
}
