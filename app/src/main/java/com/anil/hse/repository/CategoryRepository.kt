package com.anil.hse.repository

import androidx.lifecycle.MutableLiveData
import com.anil.hse.model.category.Category
import com.anil.hse.networking.ApiResponse
import com.anil.hse.networking.HseClient
import com.anil.hse.networking.message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository constructor(
    private val hseClient: HseClient
) : Repository {
    override var isLoading: Boolean = true
    suspend fun loadCategories(error: (String) -> Unit) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<List<Category>>()
        hseClient.fetchCategories { response ->
            isLoading = false
            when (response) {
                is ApiResponse.Success -> {
                    response.data?.let {
                        liveData.postValue(it.children)
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
