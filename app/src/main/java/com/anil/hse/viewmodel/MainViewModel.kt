package com.anil.hse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.anil.hse.Coroutines
import com.anil.hse.base.recyclerview.LiveCoroutinesViewModel
import com.anil.hse.datasource.HseDataSourceFactory
import com.anil.hse.model.product.Product
import com.anil.hse.networking.ApiResponse
import com.anil.hse.networking.HseClient
import com.anil.hse.networking.message
import com.anil.hse.persistance.CartDao
import com.anil.hse.persistance.entitiy.CartEntity

class MainViewModel constructor(
    private val hseClient: HseClient,
    private val cartDao: CartDao,
    private val hseDataSourceFactory: HseDataSourceFactory
) : LiveCoroutinesViewModel() {

    private var posterFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val _products = MutableLiveData<List<Product>>()
    var products: LiveData<PagedList<Product>>

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> get() = _product

    private val _cart = MutableLiveData<List<CartEntity>>()
    val cart: LiveData<List<CartEntity>> get() = _cart

    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        reloadCartItems()
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()
        products = LivePagedListBuilder(hseDataSourceFactory, config).build()
    }

    fun setCategory(catId: String) {
        hseDataSourceFactory.setCatId(catId)
    }

    private fun reloadCartItems() {
        Coroutines.ioThenMain({
            cartDao.getCartItems()
        }, {
            it?.let { items -> _cart.postValue(items) }
        })
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
        Coroutines.io {
            isAlreadyAdded?.let {
                it.quantity = quantity
                cartDao.insert(it)
            } ?: run {
                addIntoCart(product, quantity)
            }
        }
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

    fun isProductPresentInCart(product: Product): Boolean {
        val productInCart =
            cart.value?.firstOrNull { cartEntity -> cartEntity.productId == product.sku }
        return productInCart != null
    }

    fun removeItemFromCart(product: Product) {
        val productInCart =
            cart.value?.firstOrNull { cartEntity -> cartEntity.productId == product.sku }
        Coroutines.io {
            productInCart?.let {
                cartDao.deleteItem(it.id)
            }
            reloadCartItems()
        }
    }
}
