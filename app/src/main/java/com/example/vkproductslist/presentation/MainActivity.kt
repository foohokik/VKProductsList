package com.example.vkproductslist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vkproductslist.databinding.ActivityMainBinding
import com.example.vkproductslist.presentation.products.ProductsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        savedInstanceState ?: initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                binding.mainContainerView.id,
                ProductsFragment(),
                PRODUCTS_FRAGMENT_TAG
            )
            .commit()
    }

    companion object{
        const val PRODUCTS_FRAGMENT_TAG = "PRODUCT_FRAGMENT_TAG"
    }
}