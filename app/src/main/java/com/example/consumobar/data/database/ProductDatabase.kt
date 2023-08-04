package com.example.consumobar.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.consumobar.data.database.dao.ProductDao
import com.example.consumobar.data.database.entities.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun getProductDao() : ProductDao
}