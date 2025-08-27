package com.example.nutralis.data.remote

import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getProduct(barcode: String): ProductResponse {
        return api.getProductByBarcode(barcode)
    }
}