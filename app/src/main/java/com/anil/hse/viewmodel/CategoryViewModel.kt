package com.anil.hse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.anil.hse.base.LiveCoroutinesViewModel
import com.anil.hse.networking.Resource
import com.anil.hse.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers

class CategoryViewModel constructor(
    private val categoryRepository: CategoryRepository
) : LiveCoroutinesViewModel() {
    private var posterFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    var categories = posterFetchingLiveData.switchMap {
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(categoryRepository.loadCategories())
        }
    }

    fun fetchCategories() = posterFetchingLiveData.postValue(true)
}
