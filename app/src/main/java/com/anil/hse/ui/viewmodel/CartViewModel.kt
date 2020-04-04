package com.anil.hse.ui.viewmodel

import androidx.lifecycle.LiveData
import com.anil.hse.base.LiveCoroutinesViewModel
import com.anil.hse.data.persistance.entitiy.Cart
import com.anil.hse.data.repository.CartRepository

class CartViewModel constructor(
    private val cartRepository: CartRepository
) : LiveCoroutinesViewModel() {

    val cart: LiveData<List<Cart>> by lazy {
        launchOnViewModelScope {
            this.cartRepository.load()
        }
    }

    fun updateCart(cart: Cart) =
        cartRepository.update(cart)

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
