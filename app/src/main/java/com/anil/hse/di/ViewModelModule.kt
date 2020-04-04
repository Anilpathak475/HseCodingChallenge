package com.anil.hse.di

import com.anil.hse.ui.viewmodel.CartViewModel
import com.anil.hse.ui.viewmodel.CategoryViewModel
import com.anil.hse.ui.viewmodel.ProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ProductsViewModel(get(), get()) }
    viewModel { CartViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
}
