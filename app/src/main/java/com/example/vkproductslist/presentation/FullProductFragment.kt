package com.example.vkproductslist.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.vkproductslist.R
import com.example.vkproductslist.domain.model.ProductUI

class FullProductFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_full_product, container, false)
    }


    companion object {
        const val ARG_ID = "ARG_ID"

        @JvmStatic
        fun getInstance(product: ProductUI.Product): FullProductFragment {
            return FullProductFragment().apply {
                arguments =
                    bundleOf(
                        ARG_ID to product
                    )
            }
        }
    }


}