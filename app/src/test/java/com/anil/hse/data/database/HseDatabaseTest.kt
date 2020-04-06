package com.anil.hse.data.database

import android.app.Application
import androidx.room.Room
import com.anil.hse.data.persistance.AppDatabase
import com.anil.hse.data.persistance.entitiy.Cart
import com.nhaarman.mockito_kotlin.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class HseDatabaseTest {

    private lateinit var appDatabase: AppDatabase

    @Mock
    private lateinit var context: Application


    @Before
    fun initDb() {
        context = mock()
        appDatabase = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.systemContext,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun insertCartAddsDataInCartTable() {
        val cartDao = appDatabase.cartDao()
        val cart = Cart(
            productId = "dummy",
            price = "dummy",
            productName = "dummy",
            time = System.currentTimeMillis(),
            quantity = 1,
            imageUrl = "dummy"
        )
        cartDao.insert(cart)
        val cartItem = cartDao.getCartItems()
        cartItem.value?.isNotEmpty()?.let { assert(it) }
    }

    @Test
    fun updateCartUpdatesDataInCartTable() {
        val cartDao = appDatabase.cartDao()
        val cart = Cart(
            productId = "dummy",
            price = "dummy",
            productName = "dummy",
            time = System.currentTimeMillis(),
            quantity = 1,
            imageUrl = "dummy"
        )
        cartDao.insert(cart)
        cart.quantity = 5
        cartDao.insert(cart)
        cartDao.getCartItems().value?.forEach { storedCart ->
            assertEquals(cart.quantity, storedCart.quantity)
        }
    }
}