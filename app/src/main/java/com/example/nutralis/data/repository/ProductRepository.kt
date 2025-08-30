package com.example.nutralis.data.repository

import com.example.nutralis.data.remote.ApiService
import com.example.nutralis.data.remote.ProductResponse
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getProduct(barcode: String): ProductResponse {
        return api.getProductByBarcode(barcode)
    }
}