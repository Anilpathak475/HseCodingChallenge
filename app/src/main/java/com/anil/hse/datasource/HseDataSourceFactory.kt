package com.anil.hse.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.anil.hse.model.product.Product

class HseDataSourceFactory(
    private val hseDataSource: HseDataSource
) : DataSource.Factory<Int, Product>() {

    val newsDataSourceLiveData = MutableLiveData<HseDataSource>()
    override fun create(): DataSource<Int, Product> {
        newsDataSourceLiveData.postValue(hseDataSource)
        return hseDataSource
    }

    fun setCatId(catId: String) {
        this.hseDataSource.catId = catId
    }
}