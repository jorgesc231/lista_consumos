package com.example.consumobar.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consumobar.domain.DeleteListUseCase
import com.example.consumobar.domain.DeleteProductUseCase
import com.example.consumobar.domain.GetProductsUseCase
import com.example.consumobar.domain.InsertProductUseCase
import com.example.consumobar.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val insertProductUseCase: InsertProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val deleteListUseCase: DeleteListUseCase
) : ViewModel() {

    val productModel = MutableLiveData<List<Product>>()
    val isLoading = MutableLiveData<Boolean>()

    fun onCreate() {
        viewModelScope.launch {

            isLoading.postValue(true)

            val result = getProductsUseCase()

            if (result.isNotEmpty()) {
                productModel.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            isLoading.postValue(true)

            insertProductUseCase(product)
        }
    }

    fun deleteList() {
        viewModelScope.launch {
            deleteListUseCase()
            productModel.postValue(emptyList())
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productModel.postValue(
                productModel.value?.filter {
                    it.id != product.id
                }
            )
            deleteProductUseCase(product)
        }
    }
}