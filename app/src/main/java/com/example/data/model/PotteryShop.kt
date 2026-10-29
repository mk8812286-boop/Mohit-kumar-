package com.example.data.model

data class PotteryShop(
    val id: String,
    val nameHi: String,
    val nameEn: String,
    val potterNameHi: String,
    val potterNameEn: String,
    val phone: String,
    val villageCity: String,
    val districtState: String,
    val fullAddress: String,
    val pinCode: String,
    val distanceKm: Double,
    val rating: Double,
    val reviewCount: Int,
    val openingHours: String,
    val upiId: String,
    val bankAccount: String,
    val isApproved: Boolean = true,
    val specialtyHi: String,
    val specialtyEn: String,
    val yearsExperience: Int,
    val latitude: Double = 26.9124,
    val longitude: Double = 75.7873
)
