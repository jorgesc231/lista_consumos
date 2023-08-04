package com.example.consumobar.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.consumobar.data.database.entities.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM products_table")
    suspend fun getAllProducts() : List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product : ProductEntity)

    @Update
    suspend fun updateProduct(product : ProductEntity)

    @Delete
    suspend fun deleteProduct(product : ProductEntity)

    @Query("DELETE FROM products_table")
    suspend fun deleteAllProducts()
}