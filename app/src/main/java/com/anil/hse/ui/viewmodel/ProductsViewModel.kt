package com.anil.hse.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.anil.hse.base.LiveCoroutinesViewModel
import com.anil.hse.data.model.Product
import com.anil.hse.data.persistance.entitiy.Cart
import com.anil.hse.data.repository.CartRepository
import com.anil.hse.data.repository.ProductRepository
import com.anil.hse.network.Resource
import kotlinx.coroutines.Dispatchers

class ProductsViewModel constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : LiveCoroutinesViewModel() {

    private val productId
            by lazy { MutableLiveData<String>() }
    val cartNotification
            by lazy { MutableLiveData<String>() }


    val products by lazy {
        productRepository.loadProductByCategories()
    }
    var product = productId.switchMap {
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            productId.value?.let {
                emit(productRepository.loadProductDetails(productId = it))
            }
        }
    }

    val cart by lazy {
        launchOnViewModelScope {
            this.cartRepository.load()
        }
    }

    fun setCategory(catId: String) =
        productRepository.setCatId(catId)

    fun addItemInCart(product: Product, quantity: Int, result: (String) -> Unit) {
        val isAlreadyAdded =
            cart.value?.firstOrNull { cartEntity -> cartEntity.productId == product.sku }
        isAlreadyAdded?.let {
            result("Already In Database")
        } ?: run {
            cartRepository.add(createNewCartItem(product, quantity))
            result("Success")
        }
    }

    fun fetchProductProductDetail(productId: String) = this.productId.postValue(productId)

    private fun createNewCartItem(product: Product, quantity: Int) =
        Cart(
            productId = product.sku,
            productName = product.nameShort,
            price = product.productPrice.price.toString(),
            quantity = quantity,
            imageUrl = product.imageUris.first(),
            time = System.currentTimeMillis()
        )
}
