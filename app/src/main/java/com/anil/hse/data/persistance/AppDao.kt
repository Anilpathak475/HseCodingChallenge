package com.anil.hse.data.persistance

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.anil.hse.base.BaseDao
import com.anil.hse.data.persistance.entitiy.Cart

@Dao
interface CartDao : BaseDao<Cart> {

    @Query("SELECT * FROM cart where isCheckOutDone=:status")
    fun getCartItems(status: Boolean = false): LiveData<List<Cart>>
}
