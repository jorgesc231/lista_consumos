package com.example.consumobar.domain

import com.example.consumobar.data.ProductRepository
import javax.inject.Inject

class DeleteListUseCase @Inject constructor(
    private val repository : ProductRepository
) {
    suspend operator fun invoke() {
        repository.clearList()
    }
}