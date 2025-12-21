package com.example.pranish

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pranish.model.ProductModel
import com.example.pranish.repository.ProductRepoImpl
import com.example.pranish.viewmodel.ProductViewModel

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val productViewModel = remember { ProductViewModel(repo = ProductRepoImpl()) }

    val loading by productViewModel.loading.observeAsState(false)
    val allProducts by productViewModel.products.observeAsState(emptyList())

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var productToInteract by remember { mutableStateOf<ProductModel?>(null) }

    LaunchedEffect(Unit) {
        productViewModel.getAllProducts()
    }

    Scaffold{ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .padding(paddingValues)
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxHeight()
                        .background(Color.White)
                ) {
                    items(allProducts) { data ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 8.dp)) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(16.dp)
                            ) {
                                Text(data.productName)
                                Text(data.productPrice)
                                Text(data.productDesc)
                                Column(modifier = Modifier.align(Alignment.End)) {
                                    IconButton(onClick = {
                                        productToInteract = data
                                        showEditDialog = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit Product")
                                    }
                                    IconButton(onClick = {
                                        productToInteract = data
                                        showDeleteDialog = true
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete Product")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    if (showEditDialog) {
        productToInteract?.let {
            EditProductDialog(
                product = it,
                productViewModel = productViewModel,
                onDismiss = { showEditDialog = false }
            )
        }
    }

    if (showDeleteDialog) {
        productToInteract?.let {
            DeleteConfirmationDialog(
                product = it,
                productViewModel = productViewModel,
                onDismiss = { showDeleteDialog = false }
            )
        }
    }
}


@Composable
fun EditProductDialog(product: ProductModel, productViewModel: ProductViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var updatedProductName by remember { mutableStateOf(product.productName) }
    var updatedProductPrice by remember { mutableStateOf(product.productPrice) }
    var updatedProductDesc by remember { mutableStateOf(product.productDesc) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Product") },
        text = {
            Column {
                OutlinedTextField(value = updatedProductName, onValueChange = { updatedProductName = it }, label = { Text("Product Name") })
                OutlinedTextField(value = updatedProductPrice, onValueChange = { updatedProductPrice = it }, label = { Text("Product Price") })
                OutlinedTextField(value = updatedProductDesc, onValueChange = { updatedProductDesc = it }, label = { Text("Product Description") })
            }
        },
        confirmButton = {
            Button(onClick = { showConfirmDialog = true }) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm Update") },
            text = { Text("Are you sure you want to update this product?") },
            confirmButton = {
                Button(onClick = {
                    val updatedProduct = product.copy(productName = updatedProductName, productPrice = updatedProductPrice, productDesc = updatedProductDesc)
                    productViewModel.updateProduct(updatedProduct) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                    showConfirmDialog = false
                    onDismiss()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DeleteConfirmationDialog(product: ProductModel, productViewModel: ProductViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete '${product.productName}'?") },
        confirmButton = {
            Button(onClick = {
                productViewModel.deleteProduct(product.productId) { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
                onDismiss()
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}