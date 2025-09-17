package com.ofa.nutralis.ui.product

import com.ofa.nutralis.data.remote.Product

data class ProductState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
