package com.anil.hse.model.category

data class Category(
    val categoryId: Int,
    val children: List<Category>?,
    val displayName: String,
    val parentCategoryId: String?,
    val resultCount: Any
)