package com.anil.hse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.anil.hse.base.LiveCoroutinesViewModel
import com.anil.hse.model.product.Product
import com.anil.hse.networking.Resource
import com.anil.hse.persistance.entitiy.CartEntity
import com.anil.hse.repository.CartRepository
import com.anil.hse.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : LiveCoroutinesViewModel() {

    private val posterFetchingLiveData
            by lazy { MutableLiveData<Boolean>() }

    private val productId
            by lazy { MutableLiveData<String>() }


    val products by lazy {
        productRepository.loadProductByCategories()
    }
    var product = posterFetchingLiveData.switchMap {
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            productId.value?.let {
                emit(productRepository.loadProductDetails(productId = it))
            }
        }
    }

    val cart: LiveData<List<CartEntity>> by lazy {
        this.posterFetchingLiveData.switchMap {
            launchOnViewModelScope {
                this.cartRepository.load()
            }
        }
    }

    fun setCategory(catId: String) =
        productRepository.setCatId(catId)


    fun reloadCartItems() = this.posterFetchingLiveData.postValue(true)

    fun addItemInCart(product: Product, quantity: Int) {
        val isAlreadyAdded =
            cart.value?.firstOrNull { cartEntity -> cartEntity.productId == product.sku }
        val cartEntity = isAlreadyAdded?.let {
            it.quantity = quantity
            it
        } ?: run {
            createNewCartItem(product, quantity)
        }
        CoroutineScope(Dispatchers.IO).launch {
            cartRepository.add(cartEntity)
        }
    }

    fun fetchProductProductDetail(productId: String) = this.productId.postValue(productId)

    private fun createNewCartItem(product: Product, quantity: Int) = CartEntity(
        productId = product.sku,
        productName = product.nameShort,
        price = product.productPrice?.price?.toString() ?: run { "10" },
        quantity = quantity,
        imageUrl = product.imageUris.first(),
        time = System.currentTimeMillis()
    )
}
