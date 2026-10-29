package com.example.data.model.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreProduct(
    @DocumentId val id: String = "",
    val shopId: String = "",
    val ownerId: String = "",
    val nameHi: String = "",
    val nameEn: String = "",
    val descriptionHi: String = "",
    val descriptionEn: String = "",
    val category: String = "MATKA",
    val basePrice: Double = 0.0,
    val originalPrice: Double = 0.0,
    val shopNameHi: String = "",
    val shopNameEn: String = "",
    val potterNameHi: String = "",
    val potterNameEn: String = "",
    val rating: Double = 5.0,
    val reviewCount: Long = 0L,
    val finishType: String = "प्राकृतिक मिट्टी",
    val careInstructionsHi: String = "",
    val careInstructionsEn: String = "",
    val stockQuantity: Long = 0L,
    val isCustomAvailable: Boolean = true,
    val isNaturalClayCertified: Boolean = true,
    val deliveryEstimateDays: Long = 1L,
    val deliveryCharge: Double = 40.0,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
