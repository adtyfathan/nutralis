package com.example.nutralis.ui.product

import com.example.nutralis.data.remote.Product

data class ProductState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
