package com.example.vkproductslist.presentation.search

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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val repository: ProductsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), ProductListener {

  private val _queryFlow = MutableStateFlow("")
  val queryFlow = _queryFlow.asStateFlow()

  private val _showKeyboard = MutableStateFlow(true)
  val showKeyboard = _showKeyboard.asStateFlow()

  private val _searchProductsFlow = MutableStateFlow(Products())
  val searchProductsFlow = _searchProductsFlow.asStateFlow()

  private val _sideEffects = Channel<SideEffects>()
  val sideEffects = _sideEffects.receiveAsFlow()

  private var job: Job? = null

  fun clearFlowAndOnChangeKeyBoardFlag() {
    _searchProductsFlow.value = _searchProductsFlow.value.copy(products = emptyList())
    _queryFlow.value = ""
    _showKeyboard.value = false
  }

  fun getSearchProducts(searchQuery: String) {
    _queryFlow.value = searchQuery
    job?.cancel()
    job =
        viewModelScope.launch(ioDispatcher) {
          delay(500)
          val result = repository.searchProducts(searchQuery)
          result
              .onSuccess { products -> _searchProductsFlow.value = products }
              .onError { _, message ->
                _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty()))
              }
              .onException { throwable ->
                _sideEffects.send(SideEffects.ExceptionEffect(throwable))
              }
          _queryFlow.value = searchQuery
        }
  }

  override fun onClickArticle(product: ProductUI.Product) {
    viewModelScope.launch { _sideEffects.send(SideEffects.ClickEffect(product)) }
  }
}
