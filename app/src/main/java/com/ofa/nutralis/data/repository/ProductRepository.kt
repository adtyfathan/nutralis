package com.ofa.nutralis.data.repository

import com.ofa.nutralis.data.remote.ApiService
import com.ofa.nutralis.data.remote.ProductResponse
import com.ofa.nutralis.data.remote.SearchResponse
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getProduct(barcode: String): ProductResponse {
        return api.getProductByBarcode(barcode)
    }

    suspend fun searchProducts(query: String, page: Int): SearchResponse {
        return api.searchProducts(
            category = query,
            productName = query,
            page = page
        )
    }
}