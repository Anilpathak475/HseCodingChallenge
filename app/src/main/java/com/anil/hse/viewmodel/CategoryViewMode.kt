package com.anil.hse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anil.hse.base.recyclerview.LiveCoroutinesViewModel
import com.anil.hse.model.category.Category
import com.anil.hse.networking.ApiResponse
import com.anil.hse.networking.HseClient
import com.anil.hse.networking.message

class CategoryViewMode constructor(
    private val hseClient: HseClient
) : LiveCoroutinesViewModel() {

    private var posterFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories
    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    fun fetchCategories() {
        posterFetchingLiveData.postValue(true)
        hseClient.fetchCategories { response ->
            posterFetchingLiveData.postValue(false)
            when (response) {
                is ApiResponse.Success -> {
                    response.data?.let {
                        _categories.postValue(it.children)
                    }
                }
                is ApiResponse.Failure.Error -> {
                    toastLiveData.postValue(response.message())
                }
                is ApiResponse.Failure.Exception -> {
                    toastLiveData.postValue(response.message())
                }
            }
        }
    }
}
