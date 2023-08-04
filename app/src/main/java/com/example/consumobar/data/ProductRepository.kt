package com.example.consumobar.data

import com.example.consumobar.data.database.dao.ProductDao
import com.example.consumobar.data.database.entities.ProductEntity
import com.example.consumobar.data.database.entities.toDatabase
import com.example.consumobar.domain.model.Product
import com.example.consumobar.domain.model.toDomain
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {

    suspend fun getAllProcutsFromDatabase() : List<Product> {
        val response = productDao.getAllProducts()

        return response.map {it.toDomain()}
    }

    suspend fun insertProduct(product : Product) {
        productDao.insertProduct(product.toDatabase())
    }

    suspend fun deleteProduct(product : Product) {
        productDao.deleteProduct(product.toDatabase())
    }

    suspend fun clearList() {
        productDao.deleteAllProducts()
    }
}