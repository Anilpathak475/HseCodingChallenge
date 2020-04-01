package com.anil.hse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.anil.hse.base.LiveCoroutinesViewModel
import com.anil.hse.datasource.HseDataSource
import com.anil.hse.datasource.HseDataSourceFactory
import com.anil.hse.datasource.State
import com.anil.hse.model.product.Product
import com.anil.hse.persistance.entitiy.CartEntity
import com.anil.hse.repository.CartRepository
import com.anil.hse.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val hseDataSourceFactory: HseDataSourceFactory
) : LiveCoroutinesViewModel() {

    private val posterFetchingLiveData
            by lazy { MutableLiveData<Boolean>() }

    private val config by lazy {
        PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()
    }
    val products by lazy {
        LivePagedListBuilder(hseDataSourceFactory, config).build()
    }
    lateinit var product: LiveData<Product>

    val cart: LiveData<List<CartEntity>> by lazy {
        this.posterFetchingLiveData.switchMap {
            launchOnViewModelScope {
                this.cartRepository.load()
            }
        }
    }
    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    fun setCategory(catId: String) {
        hseDataSourceFactory.setCatId(catId)
    }

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

    fun fetchProductProductDetail(productId: String) {
        product = this.posterFetchingLiveData.switchMap {
            launchOnViewModelScope {
                this.productRepository.loadProductDetails(productId) {
                    this.toastLiveData.postValue(
                        it
                    )
                }
            }
        }
    }

    private fun createNewCartItem(product: Product, quantity: Int) = CartEntity(
        productId = product.sku,
        productName = product.nameShort,
        price = product.productPrice.price.toString(),
        quantity = quantity,
        imageUrl = product.imageUris.first(),
        time = System.currentTimeMillis()
    )

    val state: LiveData<State> = Transformations.switchMap<HseDataSource,
            State>(hseDataSourceFactory.hseDataSourceLiveData, HseDataSource::state)

}
