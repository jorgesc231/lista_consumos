package com.example.consumobar.domain.model

import android.os.Parcelable
import com.example.consumobar.data.database.entities.ProductEntity
import kotlinx.parcelize.Parcelize

// Modelo con el que trabaja el dominio y la UI
@Parcelize
data class Product(val id : Int = 0, val name : String, val price : Int, val quantity : Int) : Parcelable

// Convierte del modelo del entity al modelo del dominio
fun ProductEntity.toDomain() = Product(id, name, price, quantity)
