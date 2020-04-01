package com.anil.hse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.anil.hse.base.LiveCoroutinesViewModel
import com.anil.hse.model.category.Category
import com.anil.hse.repository.CategoryRepository

class CategoryViewMode constructor(
    private val categoryRepository: CategoryRepository
) : LiveCoroutinesViewModel() {
    private var posterFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val categories: LiveData<List<Category>>
    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        categories = this.posterFetchingLiveData.switchMap {
            launchOnViewModelScope {
                categoryRepository.loadCategories { this.toastLiveData.postValue(it) }
            }
        }
    }

    fun fetchCategories() = this.posterFetchingLiveData.postValue(true)
}
