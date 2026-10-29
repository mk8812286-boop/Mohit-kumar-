package com.example.data.model

data class ProductVariantSize(
    val sizeNameHi: String,
    val sizeNameEn: String,
    val capacity: String,
    val dimensions: String,
    val weight: String,
    val priceDelta: Double = 0.0
)

data class ClayColor(
    val nameHi: String,
    val nameEn: String,
    val colorHex: Long
)

data class PotteryProduct(
    val id: String,
    val nameHi: String,
    val nameEn: String,
    val descriptionHi: String,
    val descriptionEn: String,
    val category: PotteryCategory,
    val basePrice: Double,
    val originalPrice: Double,
    val shopId: String,
    val shopNameHi: String,
    val shopNameEn: String,
    val potterNameHi: String,
    val potterNameEn: String,
    val rating: Double,
    val reviewCount: Int,
    val sizes: List<ProductVariantSize>,
    val colors: List<ClayColor>,
    val finishType: String,
    val careInstructionsHi: String,
    val careInstructionsEn: String,
    val stockQuantity: Int,
    val isCustomAvailable: Boolean = true,
    val isNaturalClayCertified: Boolean = true,
    val deliveryEstimateDays: Int = 1,
    val deliveryCharge: Double = 40.0,
    val imageDrawableRes: Int? = null,
    val imageUrl: String? = null
)
