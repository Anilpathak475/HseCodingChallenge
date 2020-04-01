package com.anil.hse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.anil.hse.base.LiveCoroutinesViewModel
import com.anil.hse.persistance.entitiy.CartEntity
import com.anil.hse.repository.CartRepository

class CartViewModel constructor(
    private val cartRepository: CartRepository
) : LiveCoroutinesViewModel() {

    val cart: LiveData<List<CartEntity>>
    private var posterFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        cart = this.posterFetchingLiveData.switchMap {
            launchOnViewModelScope {
                this.cartRepository.load()
            }
        }
    }

    fun loadCart() = this.posterFetchingLiveData.postValue(true)
    fun updateCart(cartEntity: CartEntity) {
        cartRepository.update(cartEntity)
    }


    fun checkout() {
        cart.value?.apply {
            cartRepository.checkout(this)
        }
    }


    fun clearCart() {
        cart.value?.let {
            cartRepository.clear(it)
        }
    }
}
