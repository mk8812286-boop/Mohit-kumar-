package com.example.data.model.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreShop(
    @DocumentId val id: String = "",
    val ownerId: String = "",
    val nameHi: String = "",
    val nameEn: String = "",
    val potterNameHi: String = "",
    val potterNameEn: String = "",
    val phone: String = "",
    val villageCity: String = "",
    val districtState: String = "",
    val fullAddress: String = "",
    val pinCode: String = "",
    val distanceKm: Double = 0.0,
    val rating: Double = 5.0,
    val reviewCount: Long = 0L,
    val openingHours: String = "सुबह 7:00 - रात 8:30",
    val upiId: String = "",
    val bankAccount: String = "",
    val isApproved: Boolean = false,
    val specialtyHi: String = "",
    val specialtyEn: String = "",
    val yearsExperience: Long = 0L,
    val latitude: Double = 26.9124,
    val longitude: Double = 75.7873,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
