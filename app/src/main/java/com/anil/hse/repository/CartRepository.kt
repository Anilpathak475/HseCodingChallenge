package com.anil.hse.repository

import com.anil.hse.base.Coroutines
import com.anil.hse.persistance.CartDao
import com.anil.hse.persistance.entitiy.CartEntity

class CartRepository constructor(
    private val cartDao: CartDao
) : Repository {
    override var isLoading: Boolean = true

    fun load() =
        cartDao.getCartItems()

    fun add(cartEntity: CartEntity) =
        Coroutines.io {
            cartDao.insert(cartEntity)
        }

    private fun delete(cartEntity: CartEntity) =
        Coroutines.io {
            cartDao.delete(cartEntity)
        }

    private fun delete(items: List<CartEntity>) =
        Coroutines.io {
            cartDao.delete(items)
        }

    fun update(cartEntity: CartEntity) {
        if (cartEntity.quantity > 0)
            add(cartEntity)
        else
            delete(cartEntity)
    }

    fun checkout(cartItems: List<CartEntity>) {
        cartItems.apply {
            Coroutines.io {
                forEach { it.isCheckOutDone = true }
                cartDao.update(this)
            }
        }
    }

    fun clear(cartItems: List<CartEntity>) {
        delete(cartItems)
    }
}