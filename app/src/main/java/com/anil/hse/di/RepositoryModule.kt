package com.anil.hse.di

import com.anil.hse.data.datasource.HseDataSource
import com.anil.hse.data.datasource.HseDataSourceFactory
import com.anil.hse.data.repository.CartRepository
import com.anil.hse.data.repository.CategoryRepository
import com.anil.hse.data.repository.ProductRepository
import com.anil.hse.network.HseService
import com.anil.hse.network.ResponseHandler
import org.koin.dsl.module

val repositoryModule = module {
    single { CategoryRepository(get(), get()) }
    single { ProductRepository(get(), get(), get()) }
    single { CartRepository(get()) }
    single { provideHseDataSource(get(), get()) }
    single { provideHseDataSourceFactory(get()) }

}

private fun provideHseDataSource(
    hseService: HseService,
    responseHandler: ResponseHandler
): HseDataSource = HseDataSource(hseService, responseHandler)

private fun provideHseDataSourceFactory(hseDataSource: HseDataSource): HseDataSourceFactory =
    HseDataSourceFactory(hseDataSource)
