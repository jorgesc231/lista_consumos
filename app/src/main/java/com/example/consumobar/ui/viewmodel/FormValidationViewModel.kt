package com.example.consumobar.ui.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class FormValidationViewModel @Inject constructor() : ViewModel() {
    val name = MutableLiveData<String>("")
    val price = MutableLiveData<String>("")
    val quantity = MutableLiveData<String>("")

    private var name_entered = false
    private var price_entered = false
    private var quantity_entered = false

    val valid = MediatorLiveData<Boolean>().apply {
        addSource(name) {
            name_entered = it.isNotEmpty()

            value = name_entered && price_entered && quantity_entered
        }
        addSource(price) {
            price_entered = it.isNotEmpty()

            value = name_entered && price_entered && quantity_entered
        }
        addSource(quantity) {
            quantity_entered = it.isNotEmpty()

            value = name_entered && price_entered && quantity_entered
        }
    }
}