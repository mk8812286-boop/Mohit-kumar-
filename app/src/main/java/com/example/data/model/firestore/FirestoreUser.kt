package com.example.data.model.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreUser(
    @DocumentId val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val phoneNumber: String = "",
    val role: String = "CUSTOMER", // CUSTOMER, SELLER, DELIVERY, ADMIN
    val address: String = "",
    val preferredLanguage: String = "hi",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
