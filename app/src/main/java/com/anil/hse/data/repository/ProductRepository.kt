package com.anil.hse.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.anil.hse.data.datasource.HseDataSource
import com.anil.hse.data.datasource.HseDataSourceFactory
import com.anil.hse.data.datasource.State
import com.anil.hse.data.model.Product
import com.anil.hse.network.HseService
import com.anil.hse.network.Resource
import com.anil.hse.network.ResponseHandler

class ProductRepository constructor(
    private val hseService: HseService,
    private val responseHandler: ResponseHandler,
    private val hseDataSourceFactory: HseDataSourceFactory
) {

    private val config by lazy {
        PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()
    }

    suspend fun loadProductDetails(productId: String): Resource<Product> {
        return try {
            val response = hseService.fetchProductsDetails(productId)
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    fun setCatId(catId: String) =
        hseDataSourceFactory.setCatId(catId)

    fun loadProductByCategories() = LivePagedListBuilder(hseDataSourceFactory, config).build()
    val state: LiveData<State> = Transformations.switchMap<HseDataSource,
            State>(hseDataSourceFactory.hseDataSourceLiveData, HseDataSource::state)

}
