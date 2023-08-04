package com.example.consumobar.domain

import com.example.consumobar.data.ProductRepository
import com.example.consumobar.domain.model.Product
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository : ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.deleteProduct(product)
    }
}