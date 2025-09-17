package com.ofa.nutralis.data.remote

data class ProductResponse(
    val code: String,
    val product: Product
)

data class Product(
    val product_name: String?,
    val product_type: String?,
    val allergens_hierarchy: List<String>?,
    val categories_tags: List<String>?,
    val countries: String?,
    val image_url: String?,
    val ingredients: List<Ingredient>?,
    val nutrient_levels: NutrientLevels?,
    val nutriments: Nutriments?,
    val nutriscore_grade: String?,
    val nutriscore_score: Int?,
    val packaging: String?
)

data class Ingredient(
    val text: String?,
    val percent_estimate: Double?,
)

data class NutrientLevels(
    val fat: String?,
    val salt: String?,
    val `saturated-fat`: String?,
    val sugars: String?
)

data class Nutriments(
    val carbohydrates: Double?,
    val carbohydrates_unit: String?,
    val energy: Double?,
    val energy_unit: String?,
    val fat: Double?,
    val fat_unit: String?,
    val proteins: Double?,
    val proteins_unit: String?,
    val salt: Double?,
    val salt_unit: String?,
    val `saturated-fat`: Double?,
    val `saturated-fat_unit`: String?,
    val sodium: Double?,
    val sodium_unit: String?,
    val sugars: Double?,
    val sugars_unit: String?,
    val `nutrition-score-fr`: Int?,
    val `nova-group`: Int?
)
