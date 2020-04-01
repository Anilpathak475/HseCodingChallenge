package com.anil.hse.di

import com.anil.hse.repository.CartRepository
import com.anil.hse.repository.CategoryRepository
import com.anil.hse.repository.ProductRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { CategoryRepository(get()) }
    single { ProductRepository(get()) }
    single { CartRepository(get()) }
}
