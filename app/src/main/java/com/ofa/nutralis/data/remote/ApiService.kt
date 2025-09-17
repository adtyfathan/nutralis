package com.ofa.nutralis.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("product/{barcode}")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String
    ): ProductResponse

    @GET("search")
    suspend fun searchProducts(
        @Query("categories_tags_en") category: String?,
        @Query("product_name") productName: String?,
        @Query("page") page: Int,
        @Query("fields") fields: String = "code,product_name,nutrition_grades,categories_tags_en,image_url"
    ): SearchResponse
}