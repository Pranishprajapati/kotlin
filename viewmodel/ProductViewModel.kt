package com.example.pranish.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pranish.model.ProductModel
import com.example.pranish.repository.ProductRepo

class ProductViewModel(val repo: ProductRepo) : ViewModel() {
    private val _products = MutableLiveData<List<ProductModel>>()
    val products: MutableLiveData<List<ProductModel>> get() = _products

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    fun addProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit) {
        repo.addProduct(productModel, callback)
    }

    fun getAllProducts() {
        _loading.value = true
        repo.getAllProducts { success, message, productList ->
            _loading.value = false
            if (success) {
                _products.value = productList ?: emptyList()
            }
        }
    }

    fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteProduct(productId, callback)
    }

    fun updateProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit) {
        repo.updateProduct(productModel, callback)
    }
}