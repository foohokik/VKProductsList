package com.example.vkproductslist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vkproductslist.core.PRODUCTS_FRAGMENT_TAG
import com.example.vkproductslist.databinding.ActivityMainBinding
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
}