package com.anil.hse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anil.hse.Coroutines
import com.anil.hse.persistance.CartDao
import com.anil.hse.persistance.entitiy.CartEntity

class CartViewModel constructor(
    private val cartDao: CartDao
) : ViewModel() {

    private var posterFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val _cart = MutableLiveData<List<CartEntity>>()
    val cart: LiveData<List<CartEntity>> get() = _cart

    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        Coroutines.ioThenMain({
            cartDao.getCartItems()
        }, {
            it?.let { items -> _cart.postValue(items) }
        })
    }

    fun updateCart(cartEntity: CartEntity) {
        Coroutines.ioThenMain({
            if (cartEntity.quantity > 0)
                cartDao.insert(cartEntity)
            else
                cartDao.deleteItem(cartEntity.id)
            cartDao.getCartItems()
        }, {
            it?.let { items -> _cart.postValue(items) }
        })
    }
}
