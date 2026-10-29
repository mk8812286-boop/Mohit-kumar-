package com.example.data.model.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreOrderItem(
    val productId: String = "",
    val productNameHi: String = "",
    val productNameEn: String = "",
    val sizeName: String = "",
    val colorName: String = "",
    val quantity: Long = 1L,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val shopId: String = ""
)

@IgnoreExtraProperties
data class FirestoreOrder(
    @DocumentId val id: String = "",
    val orderNumber: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val deliveryAddress: String = "",
    val sellerId: String = "",
    val shopId: String = "",
    val shopName: String = "",
    val items: List<FirestoreOrderItem> = emptyList(),
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val commissionPercent: Double = 10.0,
    val commissionAmount: Double = 0.0,
    val sellerPayout: Double = 0.0,
    val totalAmount: Double = 0.0,
    val paymentMethod: String = "UPI",
    val isPaid: Boolean = false,
    val status: String = "PLACED",
    val pickupOtp: String = "4812",
    val deliveryOtp: String = "7390",
    val liveDistanceRemainingKm: Double = 0.0,
    val liveEstimatedMinutes: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
