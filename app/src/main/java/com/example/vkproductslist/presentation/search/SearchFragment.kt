package com.example.vkproductslist.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkproductslist.R
import com.example.vkproductslist.core.hideKeyboard
import com.example.vkproductslist.core.showKeyBoard
import com.example.vkproductslist.databinding.FragmentSearchBinding
import com.example.vkproductslist.presentation.SideEffects
import com.example.vkproductslist.presentation.adapter.ProductAdapter
import com.example.vkproductslist.presentation.fullproductpage.FullProductFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

  private var _binding: FragmentSearchBinding? = null
  private val binding
    get() = checkNotNull(_binding)

  private lateinit var productAdapter: ProductAdapter
  private val viewModel: SearchViewModel by viewModels()

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSearchBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initViews()
    editTextChange()
    observe()
    backArrow()
    closeEnteringSearch()
  }

  private fun editTextChange() {
    binding.editTextSearch.doOnTextChanged { text, _, _, _ ->
      text?.let {
        if (text.toString().isNotEmpty()) {
          viewModel.getSearchProducts(searchQuery = text.toString())
        }
      }
    }
  }

  private fun observe() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          viewModel.searchFlowState.collect { state ->
            productAdapter.setItems(state.product.products)
            renderKeyboard(state.keyboardState)
            renderQuery(state.query)
          }
        }
        launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
      }
    }
  }

  private fun initViews() =
      with(binding.rvSearch) {
        productAdapter = ProductAdapter(viewModel)
        val manager = LinearLayoutManager(requireContext())
        layoutManager = manager
        adapter = productAdapter
        itemAnimator = null
      }

  private fun backArrow() {
    binding.ivButtonBackSearch.setOnClickListener {
      it.context.hideKeyboard(it)
      parentFragmentManager.popBackStack()
    }
  }

  private fun closeEnteringSearch() {
    binding.imageButtonClose.setOnClickListener {
      with(binding.editTextSearch) {
        text.clear()
        clearFocus()
        isCursorVisible = false
      }
      activity?.hideKeyboard()
      viewModel.clearFlowAndOnChangeKeyBoardFlag()
      productAdapter.setItems(emptyList())
    }
  }

  private fun renderQuery(text: String) {
    if (text != binding.editTextSearch.text.toString()) {
      binding.editTextSearch.setText(text)
    }
  }

  private fun renderKeyboard(isShow: Boolean) {
    if (isShow) {
      with(binding.editTextSearch) {
        requestFocus()
        context.showKeyBoard(this)
      }
    } else {
      activity?.hideKeyboard()
    }
  }

  private fun handleSideEffects(sideEffects: SideEffects) {
    when (sideEffects) {
      is SideEffects.ErrorEffect -> {
        Toast.makeText(
                requireContext(), getString(R.string.error, sideEffects.err), Toast.LENGTH_LONG)
            .show()
      }
      is SideEffects.ExceptionEffect -> {
        Toast.makeText(
                requireContext(),
                getString(R.string.error, sideEffects.throwable.message),
                Toast.LENGTH_LONG)
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

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
