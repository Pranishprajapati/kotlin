package com.example.pranish.repository

import com.example.pranish.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductRepoImpl : ProductRepo {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref: DatabaseReference = database.getReference("products")

    override fun addProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        val modelWithId = productModel.copy(productId = id)

        ref.child(id).setValue(modelWithId).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product added successfully")
            } else {
                callback(false, "Error: ${it.exception?.message}")
            }
        }
    }

    override fun getAllProducts(callback: (Boolean, String, List<ProductModel>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductModel>()
                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(ProductModel::class.java)
                        if (product != null) {
                            productList.add(product)
                        }
                    }
                    callback(true, "Products fetched successfully", productList)
                } else {
                    callback(true, "No products found", emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, "Error: ${error.message}", null)
            }
        })
    }

    override fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        ref.child(productId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product deleted successfully")
            } else {
                callback(false, "Error: ${it.exception?.message}")
            }
        }
    }

    override fun updateProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit) {
        ref.child(productModel.productId).setValue(productModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product updated successfully")
            } else {
                callback(false, "Error: ${it.exception?.message}")
            }
        }
    }
}