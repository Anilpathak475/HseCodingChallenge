package com.anil.hse.repository

import androidx.lifecycle.MutableLiveData
import com.anil.hse.persistance.CartDao
import com.anil.hse.persistance.entitiy.CartEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository constructor(
    private val cartDao: CartDao
) {

    suspend fun loadDisneyPosters() = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<List<CartEntity>>()
        cartDao.getCartItems()
    }
}
