package com.anil.hse.data.model

data class Category(
    val categoryId: Int = 0,
    val children: List<Category>? = null,
    val displayName: String = "",
    val parentCategoryId: String? = "",
    val resultCount: Int? = 0
)