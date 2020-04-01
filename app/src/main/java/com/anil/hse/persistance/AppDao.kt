package com.anil.hse.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anil.hse.persistance.entitiy.CartEntity

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cartEntity: CartEntity)

    @Query("SELECT * FROM cart")
    fun getCartItems(): List<CartEntity>

    @Query(" DELETE FROM cart")
    fun deleteAll()

    @Query(" DELETE FROM cart where id=:id")
    fun deleteItem(id: Int)
}
