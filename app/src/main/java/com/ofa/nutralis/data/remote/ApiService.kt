package com.ofa.nutralis.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("product/{barcode}")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String
    ): ProductResponse
}