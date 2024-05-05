package com.example.vkproductslist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkproductslist.R
import com.example.vkproductslist.presentation.adapter.ProductAdapter
import com.example.vkproductslist.databinding.FragmentProductsBinding
import com.example.vkproductslist.presentation.pagination.OnScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment() {

  private var _binding: FragmentProductsBinding? = null
  private val binding
    get() = checkNotNull(_binding)

  private lateinit var productAdapter: ProductAdapter
  private val viewModel: ProductsViewModel by viewModels()
  private val onScrollListener by lazy {
    OnScrollListener(
      viewModel, binding.rvProducts.layoutManager as LinearLayoutManager
    )
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    _binding = FragmentProductsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initViews()
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch { viewModel.productsFlow.collect { productAdapter.setItems(it.products) } }
        launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
      }
    }
  }

  private fun initViews() =
      with(binding.rvProducts) {
        productAdapter = ProductAdapter(viewModel)
        adapter = productAdapter
        itemAnimator = null
        addOnScrollListener(onScrollListener)
      }

  private fun handleSideEffects(sideEffects: SideEffects) {
    when (sideEffects) {
      is SideEffects.ErrorEffect -> {
        Toast.makeText(requireContext(), "Ошибка: " + sideEffects.err, Toast.LENGTH_LONG).show()
      }
      is SideEffects.ExceptionEffect -> {
        Toast.makeText(
                requireContext(), "Ошибка: " + sideEffects.throwable.message, Toast.LENGTH_LONG)
            .show()
      }
      is SideEffects.ClickEffect -> {
        parentFragmentManager
          .beginTransaction()
          .addToBackStack(null)
          .replace(R.id.main_container_view, FullProductFragment.getInstance(sideEffects.product))
          .commit()
      }
      else -> {}
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.rvProducts.removeOnScrollListener(onScrollListener)
    _binding = null
  }
}
