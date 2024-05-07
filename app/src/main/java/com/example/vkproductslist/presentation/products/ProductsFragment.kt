package com.example.vkproductslist.presentation.products

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
import com.example.vkproductslist.databinding.FragmentProductsBinding
import com.example.vkproductslist.presentation.SideEffects
import com.example.vkproductslist.presentation.adapter.ProductAdapter
import com.example.vkproductslist.presentation.fullproductpage.FullProductFragment
import com.example.vkproductslist.presentation.pagination.OnScrollListener
import com.example.vkproductslist.presentation.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment() {

  private var _binding: FragmentProductsBinding? = null
  private val binding
    get() = checkNotNull(_binding)

  private lateinit var productAdapter: ProductAdapter
  private val viewModel: ProductsViewModel by viewModels()
  private var onScrollListener: OnScrollListener? = null

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
    navigateToSearch()
    observe()
  }

  private fun observe() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch { viewModel.productsFlow.collect { productAdapter.setItems(it.products) } }
        launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
          launch { viewModel.stateProgressBar.collect(::renderLoading) }
      }
    }
  }

  private fun initViews() =
      with(binding.rvProducts) {
        productAdapter = ProductAdapter(viewModel)
        val manager = LinearLayoutManager(requireContext())
        layoutManager = manager
        adapter = productAdapter
        itemAnimator = null
        onScrollListener = OnScrollListener(viewModel, manager)
        onScrollListener?.let { listener -> addOnScrollListener(listener) }
      }

  private fun navigateToSearch() {
    binding.imageButtonSearch.setOnClickListener {
      parentFragmentManager
          .beginTransaction()
          .addToBackStack(null)
          .replace(R.id.main_container_view, SearchFragment())
          .commit()
    }
  }

  private fun handleSideEffects(sideEffects: SideEffects) {
    when (sideEffects) {
      is SideEffects.ErrorEffect -> {
        Toast.makeText(requireContext(), getString(R.string.error, sideEffects.err), Toast.LENGTH_LONG).show()
      }
      is SideEffects.ExceptionEffect -> {
        Toast.makeText(
                requireContext(), getString(R.string.error, sideEffects.throwable.message), Toast.LENGTH_LONG )
            .show()
      }
      is SideEffects.ClickEffect -> {
        parentFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_container_view, FullProductFragment.getInstance(sideEffects.product))
            .commit()
      }
    }
  }

    private fun renderLoading (isLoading: Boolean) {
        if (!isLoading) {
            with(binding){
                progressBar.visibility = View.GONE
                rvProducts.visibility = View.VISIBLE
            }
        }
    }

  override fun onDestroyView() {
    super.onDestroyView()
    onScrollListener?.let { binding.rvProducts.removeOnScrollListener(it) }
    _binding = null
  }
}
