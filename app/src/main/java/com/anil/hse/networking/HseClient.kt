package com.anil.hse.networking

import com.anil.hse.model.category.Category
import com.anil.hse.model.product.Product
import com.anil.hse.model.product.ProductResponse

class HseClient(private val hseService: HseService) {

    fun fetchCategories(
        onResult: (response: ApiResponse<Category>) -> Unit
    ) =
        this.hseService.fetchCategoryTree().transform(onResult)


    fun fetchProductByCategory(
        categoryId: String, page: Int = 1, pageSeize: Int = 20,
        onResult: (response: ApiResponse<ProductResponse>) -> Unit
    ) =
        this.hseService.fetchProductsCategory(categoryId, page, pageSeize).transform(onResult)

    fun fetchProductDetails(
        productId: String,
        onResult: (response: ApiResponse<Product>) -> Unit
    ) = hseService.fetchProductsDetails(productId).transform(onResult)

}
