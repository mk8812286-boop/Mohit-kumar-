package com.example.data.model

data class Complaint(
    val id: String,
    val orderNumber: String,
    val productName: String,
    val issueType: String,
    val description: String,
    val resolutionType: String, // Refund or Replacement
    val status: String = "Pending (विचाराधीन)",
    val timestamp: Long = System.currentTimeMillis(),
    val potterShopName: String,
    val photoSimulated: Boolean = true,
    val refundAmount: Double = 0.0
)

data class CustomOrderRequest(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val potterShopName: String,
    val description: String,
    val dimensionsAndCapacity: String,
    val targetBudget: Double,
    val status: String = "Under Review (समीक्षाधीन)",
    val timestamp: Long = System.currentTimeMillis()
)
