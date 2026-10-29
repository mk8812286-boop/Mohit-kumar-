package com.example.data.model

enum class OrderStatus(val titleHi: String, val titleEn: String, val stepIndex: Int) {
    PLACED("ऑर्डर दर्ज (Order Placed)", "Order Placed", 0),
    SELLER_ACCEPTED("कुम्हार ने स्वीकार किया (Accepted)", "Seller Accepted", 1),
    PACKING("पैकिंग जारी (Packing)", "Packing in Progress", 2),
    READY_FOR_PICKUP("पिकअप हेतु तैयार (Ready for Pickup)", "Ready for Pickup", 3),
    PARTNER_ASSIGNED("डिलीवरी साथी तय (Partner Assigned)", "Partner Assigned", 4),
    PICKED_UP("पिकअप संपन्न (Product Picked Up)", "Product Picked Up", 5),
    OUT_FOR_DELIVERY("रास्ते में है (Out for Delivery)", "Out for Delivery", 6),
    DELIVERED("सफलतापूर्वक डिलीवर (Delivered)", "Delivered", 7),
    CANCELLED("रद्द (Cancelled)", "Cancelled", -1)
}

enum class PaymentMethod(val titleHi: String, val titleEn: String) {
    UPI("UPI (गूगल पे / फोन पे / पेटीएम)", "UPI (GPay / PhonePe / Paytm)"),
    COD("कैश ऑन डिलीवरी (COD)", "Cash on Delivery (COD)"),
    CARD("क्रेडिट / डेबिट कार्ड", "Credit / Debit Card")
}

data class OrderItem(
    val product: PotteryProduct,
    val selectedSize: ProductVariantSize,
    val selectedColor: ClayColor,
    val quantity: Int,
    val unitPrice: Double
)

data class Order(
    val id: String,
    val orderNumber: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val items: List<OrderItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val commissionPercent: Double = 10.0,
    val commissionAmount: Double,
    val sellerPayout: Double,
    val totalAmount: Double,
    val paymentMethod: PaymentMethod,
    val isPaid: Boolean = false,
    val status: OrderStatus = OrderStatus.PLACED,
    val orderTimestamp: Long = System.currentTimeMillis(),
    val deliveryBoyName: String = "सुरेश कुमार (Suresh Kumar)",
    val deliveryBoyPhone: String = "+91 98290 12345",
    val pickupOtp: String = "4812",
    val deliveryOtp: String = "7390",
    val packingPhotoTaken: Boolean = false,
    val liveDistanceRemainingKm: Double = 3.2,
    val liveEstimatedMinutes: Int = 18
)
