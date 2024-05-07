package com.example.vkproductslist.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkproductslist.core.network.onError
import com.example.vkproductslist.core.network.onException
import com.example.vkproductslist.core.network.onSuccess
import com.example.vkproductslist.di.IoDispatcher
import com.example.vkproductslist.domain.ProductsRepository
import com.example.vkproductslist.domain.model.ProductUI
import com.example.vkproductslist.presentation.SideEffects
import com.example.vkproductslist.presentation.adapter.ProductListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val repository: ProductsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), ProductListener {

  private val _searchFlowState = MutableStateFlow(SearchScreenState())
  val searchFlowState = _searchFlowState.asStateFlow()

  private val _sideEffects = Channel<SideEffects>()
  val sideEffects = _sideEffects.receiveAsFlow()

  private var job: Job? = null

  private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    viewModelScope.launch { _sideEffects.send(SideEffects.ExceptionEffect(throwable)) }
  }

  fun clearFlowAndOnChangeKeyBoardFlag() {
    _searchFlowState.update { state ->
      state.copy(
          product = state.product.copy(products = emptyList()), query = "", keyboardState = false)
    }
  }

  fun getSearchProducts(searchQuery: String) {
    _searchFlowState.update { state -> state.copy(query = searchQuery) }
    job?.cancel()
    job =
        viewModelScope.launch(exceptionHandler) {
          withContext(ioDispatcher) {
            delay(DELAY)
            val result = repository.searchProducts(searchQuery)
            result
                .onSuccess { _searchFlowState.update { state -> state.copy(product = it) } }
                .onError { _, message ->
                  _sideEffects.send(SideEffects.ErrorEffect(message.orEmpty()))
                }
                .onException { throwable ->
                  _sideEffects.send(SideEffects.ExceptionEffect(throwable))
                }
            _searchFlowState.update { state -> state.copy(query = searchQuery) }
          }
        }
  }

  override fun onClickArticle(product: ProductUI.Product) {
    viewModelScope.launch { _sideEffects.send(SideEffects.ClickEffect(product)) }
  }

  companion object {
    const val DELAY = 500L
  }
}
