package com.example.vkproductslist.di

import com.example.vkproductslist.data.repository.ProductsRepoImpl
import com.example.vkproductslist.domain.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent


@Module
@InstallIn(ActivityRetainedComponent::class)
interface MainModule {

    @Binds
    fun bindMainRepository(repository: ProductsRepoImpl): ProductsRepository
}