package com.anil.hse.networking

import com.anil.hse.model.category.Category
import com.anil.hse.model.product.ProductResponse
import com.anil.hse.model.product.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface HseService {

    @GET("ext-api/app/1/category/tree")
    fun fetchCategoryTree(): Call<Category>

    @GET("ext-api/app/1/c/**/*-{categoryId}")
    fun fetchProductsCategory(@Path("categoryId") categoryId: String): Call<ProductResponse>

    @GET("ext-api/app/1/product/{productId}")
    fun fetchProductsDetails(@Path("productId") productId: String): Call<Product>
}
