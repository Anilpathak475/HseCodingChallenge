package com.anil.hse.networking

import com.anil.hse.model.category.Category
import com.anil.hse.model.product.ProductResponse
import com.anil.hse.model.product.Product

class HseClient(private val hseService: HseService) {

    fun fetchCategories(
        onResult: (response: ApiResponse<Category>) -> Unit
    ) {
        this.hseService.fetchCategoryTree().transform(onResult)
    }

    fun fetchProductByCategory(
        categoryId: String,
        onResult: (response: ApiResponse<ProductResponse>) -> Unit
    ) {
        this.hseService.fetchProductsCategory(categoryId).transform(onResult)
    }

    fun fetchProductDetails(
        productId: String,
        onResult: (response: ApiResponse<Product>) -> Unit
    ) {
        this.hseService.fetchProductsDetails(productId).transform(onResult)
    }
}
