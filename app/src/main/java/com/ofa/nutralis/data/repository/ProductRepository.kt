package com.ofa.nutralis.data.repository

import com.ofa.nutralis.data.remote.ApiService
import com.ofa.nutralis.data.remote.ProductResponse
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getProduct(barcode: String): ProductResponse {
        return api.getProductByBarcode(barcode)
    }
}