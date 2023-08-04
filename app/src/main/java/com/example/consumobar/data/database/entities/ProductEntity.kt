package com.example.consumobar.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.consumobar.domain.model.Product

// the primary key must be 0 for Room to treat it as unset. If you use any other default value (e.g. -1), Room will not autogenerate the id

@Entity(tableName = "products_table")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id : Int = 0,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "price") val price : Int,
    @ColumnInfo(name = "quantity") val quantity : Int
)

fun Product.toDatabase() = ProductEntity(id, name, price, quantity)