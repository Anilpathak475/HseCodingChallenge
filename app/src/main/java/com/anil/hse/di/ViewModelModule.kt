package com.anil.hse.di

import com.anil.hse.viewmodel.CartViewModel
import com.anil.hse.viewmodel.CategoryViewMode
import com.anil.hse.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { CartViewModel(get()) }
    viewModel { CategoryViewMode(get()) }

}
