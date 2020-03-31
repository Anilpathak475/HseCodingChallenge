package com.anil.hse.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.anil.hse.model.product.Product
import com.anil.hse.persistance.entitiy.CartEntity
import com.google.gson.Gson

@Database(
    entities = [CartEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

/*
    abstract fun categoryDao(): CategoryDao
*/
    abstract fun cartDao(): CartDao
}