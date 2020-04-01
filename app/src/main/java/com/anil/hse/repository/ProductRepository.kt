package com.anil.hse.repository

import androidx.lifecycle.MutableLiveData
import com.anil.hse.model.product.Product
import com.anil.hse.networking.ApiResponse
import com.anil.hse.networking.HseClient
import com.anil.hse.networking.message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository constructor(
    private val hseClient: HseClient
) : Repository {
    override var isLoading: Boolean = true
    suspend fun loadProductDetails(productId: String, error: (String) -> Unit) =
        withContext(Dispatchers.IO) {
            isLoading = true
            val liveData = MutableLiveData<Product>()
            hseClient.fetchProductDetails(productId) { response ->
                isLoading = false
                when (response) {
                    is ApiResponse.Success -> {
                        response.data?.let {
                            liveData.postValue(it)
                        }
                    }
                    is ApiResponse.Failure.Error -> {
                        error(response.message())
                    }
                    is ApiResponse.Failure.Exception -> {
                        error(response.message())
                    }
                }
            }
            liveData
        }

}
