package com.anil.hse.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anil.hse.Coroutines
import com.anil.hse.model.category.Category
import com.anil.hse.model.product.Product
import com.anil.hse.networking.ApiResponse
import com.anil.hse.networking.HseClient
import com.anil.hse.networking.message
import com.anil.hse.persistance.CartDao
import com.anil.hse.persistance.entitiy.CartEntity

class MainViewModel constructor(
    private val hseClient: HseClient,
    private val cartDao: CartDao
) : ViewModel() {

    private var posterFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> get() = _product

    private val _cart = MutableLiveData<List<CartEntity>>()
    val cart: LiveData<List<CartEntity>> get() = _cart

    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        reloadCartItems()
    }

    private fun reloadCartItems() {
        Coroutines.ioThenMain({
            cartDao.getCartItems()
        }, {
            it?.let { items -> _cart.postValue(items) }
        })
    }

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

    fun fetchProductsByCat(categoryId: String) {
        posterFetchingLiveData.postValue(true)
        hseClient.fetchProductByCategory(categoryId) { response ->
            posterFetchingLiveData.postValue(false)
            when (response) {
                is ApiResponse.Success -> {
                    response.data?.let {
                        _products.postValue(it.products)
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

    fun fetchProductProductDetail(productId: String) {
        posterFetchingLiveData.postValue(true)
        hseClient.fetchProductDetails(productId) { response ->
            posterFetchingLiveData.postValue(false)
            when (response) {
                is ApiResponse.Success -> {
                    response.data?.let {
                        _product.postValue(it)
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

    fun addItemInCart(product: Product, quantity: Int) {
        val isAlreadyAdded =
            cart.value?.firstOrNull { cartEntity -> cartEntity.productId == product.sku }
        Coroutines.ioThenMain({
            isAlreadyAdded?.let {
                it.quantity = quantity
                cartDao.insert(it)
            } ?: run {
                addIntoCart(product, quantity)
            }
            cartDao.getCartItems()
        }, {
            it?.let { items -> _cart.postValue(items) }
        })


    }

    private fun addIntoCart(product: Product, quantity: Int) {
        cartDao.insert(
            CartEntity(
                productId = product.sku,
                productName = product.nameShort,
                price = product.productPrice.price.toString(),
                quantity = quantity,
                imageUrl = product.imageUris.first(),
                time = System.currentTimeMillis()
            )
        )
    }
}
