package com.example.vkproductslist.presentation.fullproductpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.vkproductslist.R
import com.example.vkproductslist.databinding.FragmentFullProductBinding
import com.example.vkproductslist.domain.model.ProductUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FullProductFragment : Fragment() {

  private var _binding: FragmentFullProductBinding? = null
  private val binding
    get() = checkNotNull(_binding)

  private val viewModel: FullProductViewModel by viewModels()

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFullProductBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initListeners()
    observe()
  }

  private fun observe() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch { viewModel.contentScreen.collect(::setContent) }
      }
    }
  }

  private fun setContent(state: ProductUI.Product) =
      with(binding) {
        Glide.with(collapsingIv.context).load(state.thumbnail).into(collapsingIv)
        tvTitleFullProduct.text = state.title
        tvFullProductDescription.text = state.description
        tvFullPrice.text = getString(R.string.price, state.price)
      }

  private fun initListeners() {
    binding.imageButtonBack.setOnClickListener { parentFragmentManager.popBackStack() }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    const val ARG_ID = "ARG_ID"
    @JvmStatic
    fun getInstance(product: ProductUI.Product): FullProductFragment {
      return FullProductFragment().apply { arguments = bundleOf(ARG_ID to product) }
    }
  }
}
