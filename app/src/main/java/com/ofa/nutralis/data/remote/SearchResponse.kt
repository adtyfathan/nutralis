package com.ofa.nutralis.data.remote

data class SearchResponse(
    val count: Int,
    val page: Int,
    val page_count: Int,
    val page_size: Int,
    val skip: Int,
    val products: List<ProductItem>
)

data class ProductItem(
    val code: String,
    val product_name: String?,
    val nutrition_grades: String?,
    val categories_tags_en: List<String>?,
    val image_url: String?
)

