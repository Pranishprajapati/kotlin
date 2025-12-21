package com.example.pranish.repository

import com.example.pranish.model.ProductModel

interface ProductRepo {
    fun addProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit)
    fun getAllProducts(callback: (Boolean, String, List<ProductModel>?) -> Unit)
    fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit)
    fun updateProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit)
}