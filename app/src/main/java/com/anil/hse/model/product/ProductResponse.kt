package com.anil.hse.model.product

import com.anil.hse.model.category.Category
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val activeSorting: ActiveSorting,
    val brands: List<Brand>,
    val cachingForbidden: Boolean,
    val categories: List<Category>,
    val displayName: Any,
    val filter: Filter,
    val paging: Paging,
    @SerializedName("productResults")
    val products: List<Product>,
    val resultCount: Int,
    val sortings: List<Sorting>,
    val topShop: String
)

data class Paging(
    val numPages: Int,
    val page: Int,
    val pageSize: Int
)
data class Sorting(
    val displayName: String,
    val name: String,
    val order: String,
    val sortType: String
)


data class Brand(
    val brandId: String,
    val children: Any,
    val displayName: String,
    val resultCount: Int
)


data class ActiveSorting(
    val displayName: Any,
    val name: String,
    val order: String,
    val sortType: String
)

data class Filter(
    val filterGroups: List<FilterGroup>,
    val resetLink: String,
    val selectedFilterItems: List<Any>,
    val selectedItemCount: Int
)

data class FilterGroup(
    val displayName: String,
    val displayType: Any,
    val fieldName: String,
    val filterItems: List<FilterItem>,
    val filterName: String,
    val hidden: Boolean,
    val name: String,
    val resetLink: String
)

data class FilterItem(
    val childCount: Int,
    val displayName: String,
    val filterName: String,
    val filterValue: String,
    val iconImgUrl: Any,
    val id: Any,
    val level: Int,
    val link: String,
    val name: String,
    val resetLink: String,
    val resultCount: Int,
    val rgbCode: Any,
    val selected: Boolean
)