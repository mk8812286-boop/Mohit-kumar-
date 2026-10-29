package com.example.data.model

enum class UserRole(val titleHi: String, val titleEn: String) {
    CUSTOMER("ग्राहक (Customer)", "Customer"),
    SELLER("कुम्हार विक्रेता (Kumhar Seller)", "Kumhar Seller"),
    DELIVERY("डिलीवरी साथी (Delivery Partner)", "Delivery Partner"),
    ADMIN("सुपर एडमिन (Super Admin)", "Super Admin")
}

enum class AppLanguage(val code: String, val displayName: String) {
    HINDI("hi", "हिंदी"),
    ENGLISH("en", "English")
}
