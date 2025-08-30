package com.example.nutralis.data.model

data class ScannedProduct(
    val product_name: String? = null,
    val product_type: String? = null,
    val image_url: String? = null,
    val nutriscore_grade: String? = null,
    val nutriscore_score: Int? = null,
    val code: String = "",
    val user_id: String = ""
)
