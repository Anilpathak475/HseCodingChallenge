package com.anil.hse.data.repository

import com.anil.hse.data.model.Category
import com.anil.hse.network.HseService
import com.anil.hse.network.Resource
import com.anil.hse.network.ResponseHandler

class CategoryRepository constructor(
    private val hseService: HseService,
    private val responseHandler: ResponseHandler
) {
    suspend fun loadCategories(): Resource<Category> {
        return try {
            val response = hseService.fetchCategoryTree()
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


}
