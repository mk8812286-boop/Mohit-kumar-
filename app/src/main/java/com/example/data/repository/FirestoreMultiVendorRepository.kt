package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.firestore.FirestoreOrder
import com.example.data.model.firestore.FirestoreOrderItem
import com.example.data.model.firestore.FirestoreProduct
import com.example.data.model.firestore.FirestoreShop
import com.example.data.model.firestore.FirestoreUser
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class FirestoreMultiVendorRepository(
    private val db: FirebaseFirestore
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before writing to Firestore.")
    }

    // ==========================================
    // 1. USERS COLLECTION (users/{userId})
    // ==========================================

    fun observeCurrentUser(): Flow<FirestoreUser?> = flow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            emit(null)
            return@flow
        }
        val docRef = db.collection("users").document(uid)
        emitAll(
            docRef.snapshots()
                .map { snapshot -> snapshot.toObject(FirestoreUser::class.java) }
                .catch { e ->
                    if (e is Exception) handleFirestoreError(e, OperationType.GET, docRef.path)
                    emit(null)
                }
        )
    }

    suspend fun createOrUpdateUser(user: FirestoreUser) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        val payload = mapOf(
            "id" to uid,
            "email" to user.email.ifBlank { auth.currentUser?.email ?: "" },
            "displayName" to user.displayName.ifBlank { auth.currentUser?.displayName ?: "User" },
            "phoneNumber" to user.phoneNumber,
            "role" to user.role,
            "address" to user.address,
            "preferredLanguage" to user.preferredLanguage,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    // ==========================================
    // 2. SHOPS: ADD NEW SHOPS & OBSERVE
    // ==========================================

    fun observeShops(): Flow<List<FirestoreShop>> = flow {
        val collection = db.collection("shops")
        emitAll(
            collection.snapshots()
                .map { snapshot -> snapshot.toObjects(FirestoreShop::class.java) }
                .catch { e ->
                    if (e is Exception) handleFirestoreError(e, OperationType.LIST, collection.path)
                    emit(emptyList())
                }
        )
    }

    /**
     * Adds a new Kumhar pottery workshop to the marketplace.
     */
    suspend fun addNewShop(shop: FirestoreShop): String {
        val uid = requireUserId()
        val collection = db.collection("shops")
        val docRef = if (shop.id.isNotBlank()) collection.document(shop.id) else collection.document()
        val payload = mapOf(
            "id" to docRef.id,
            "ownerId" to uid,
            "nameHi" to shop.nameHi,
            "nameEn" to shop.nameEn,
            "potterNameHi" to shop.potterNameHi,
            "potterNameEn" to shop.potterNameEn,
            "phone" to shop.phone,
            "villageCity" to shop.villageCity,
            "districtState" to shop.districtState,
            "fullAddress" to shop.fullAddress,
            "pinCode" to shop.pinCode,
            "distanceKm" to shop.distanceKm,
            "rating" to shop.rating,
            "reviewCount" to shop.reviewCount,
            "openingHours" to shop.openingHours,
            "upiId" to shop.upiId,
            "bankAccount" to shop.bankAccount,
            "isApproved" to shop.isApproved,
            "specialtyHi" to shop.specialtyHi,
            "specialtyEn" to shop.specialtyEn,
            "yearsExperience" to shop.yearsExperience,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
            return docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    // ==========================================
    // 3. PRODUCTS: FETCH PRODUCTS BY LOCATION
    // ==========================================

    /**
     * Fetches pottery products filtered by customer location and maximum radius distance in kilometers.
     * Combines nearby shops within the radius and streams real-time matching pottery.
     */
    fun fetchProductsByLocation(
        maxDistanceKm: Double = 10.0,
        cityOrDistrict: String? = null,
        category: String? = null
    ): Flow<List<FirestoreProduct>> {
        val shopsFlow = observeShops()
        val productsFlow = observeProducts(category)

        return combine(shopsFlow, productsFlow) { allShops, allProducts ->
            val nearbyShopIds = allShops.filter { shop ->
                val distanceMatches = maxDistanceKm >= 20.0 || shop.distanceKm <= maxDistanceKm
                val locationMatches = cityOrDistrict.isNullOrBlank() ||
                        shop.districtState.contains(cityOrDistrict, ignoreCase = true) ||
                        shop.villageCity.contains(cityOrDistrict, ignoreCase = true)
                distanceMatches && locationMatches
            }.map { it.id }.toSet()

            allProducts.filter { product ->
                nearbyShopIds.contains(product.shopId) || maxDistanceKm >= 20.0
            }
        }
    }

    fun observeProducts(category: String? = null): Flow<List<FirestoreProduct>> = flow {
        val collection = db.collection("products")
        val query = if (category != null && category != "all" && category != "ALL") {
            collection.whereEqualTo("category", category)
        } else {
            collection
        }
        emitAll(
            query.snapshots()
                .map { snapshot -> snapshot.toObjects(FirestoreProduct::class.java) }
                .catch { e ->
                    if (e is Exception) handleFirestoreError(e, OperationType.LIST, collection.path)
                    emit(emptyList())
                }
        )
    }

    suspend fun addProduct(product: FirestoreProduct): String {
        val uid = requireUserId()
        val collection = db.collection("products")
        val docRef = if (product.id.isNotBlank()) collection.document(product.id) else collection.document()
        val payload = mapOf(
            "id" to docRef.id,
            "shopId" to product.shopId,
            "ownerId" to uid,
            "nameHi" to product.nameHi,
            "nameEn" to product.nameEn,
            "descriptionHi" to product.descriptionHi,
            "descriptionEn" to product.descriptionEn,
            "category" to product.category,
            "basePrice" to product.basePrice,
            "originalPrice" to product.originalPrice,
            "shopNameHi" to product.shopNameHi,
            "shopNameEn" to product.shopNameEn,
            "potterNameHi" to product.potterNameHi,
            "potterNameEn" to product.potterNameEn,
            "rating" to product.rating,
            "reviewCount" to product.reviewCount,
            "finishType" to product.finishType,
            "careInstructionsHi" to product.careInstructionsHi,
            "careInstructionsEn" to product.careInstructionsEn,
            "stockQuantity" to product.stockQuantity,
            "isCustomAvailable" to product.isCustomAvailable,
            "isNaturalClayCertified" to product.isNaturalClayCertified,
            "deliveryEstimateDays" to product.deliveryEstimateDays,
            "deliveryCharge" to product.deliveryCharge,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
            return docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    // ==========================================
    // 4. ORDER CREATION LOGIC (orders/{orderId})
    // ==========================================

    /**
     * Creates a new customer order with multi-vendor commission calculation and stock deduction.
     * Computes platform commission (10%) and net artisan payout (90%).
     */
    suspend fun createOrder(
        customerName: String,
        customerPhone: String,
        deliveryAddress: String,
        shopId: String,
        shopName: String,
        sellerId: String,
        items: List<FirestoreOrderItem>,
        paymentMethod: String = "UPI",
        commissionPercent: Double = 10.0
    ): FirestoreOrder {
        val uid = requireUserId()
        val subtotal = items.sumOf { it.unitPrice * it.quantity }
        val deliveryFee = if (subtotal >= 500.0 || items.isEmpty()) 0.0 else 40.0
        val commissionAmount = subtotal * (commissionPercent / 100.0)
        val sellerPayout = subtotal - commissionAmount
        val totalAmount = subtotal + deliveryFee

        val orderDocRef = db.collection("orders").document()
        val orderNumber = "PK-${Random.nextInt(1000, 9999)}"
        val pickupOtp = "${Random.nextInt(1000, 9999)}"
        val deliveryOtp = "${Random.nextInt(1000, 9999)}"

        val mappedItems = items.map {
            mapOf(
                "productId" to it.productId,
                "productNameHi" to it.productNameHi,
                "productNameEn" to it.productNameEn,
                "sizeName" to it.sizeName,
                "colorName" to it.colorName,
                "quantity" to it.quantity,
                "unitPrice" to it.unitPrice,
                "totalPrice" to (it.unitPrice * it.quantity),
                "shopId" to it.shopId
            )
        }

        val orderPayload = mapOf(
            "id" to orderDocRef.id,
            "orderNumber" to orderNumber,
            "customerId" to uid,
            "customerName" to customerName.ifBlank { "ग्राहक" },
            "customerPhone" to customerPhone,
            "deliveryAddress" to deliveryAddress,
            "sellerId" to sellerId,
            "shopId" to shopId,
            "shopName" to shopName,
            "items" to mappedItems,
            "subtotal" to subtotal,
            "deliveryFee" to deliveryFee,
            "commissionPercent" to commissionPercent,
            "commissionAmount" to commissionAmount,
            "sellerPayout" to sellerPayout,
            "totalAmount" to totalAmount,
            "paymentMethod" to paymentMethod,
            "isPaid" to (paymentMethod != "COD"),
            "status" to "PLACED",
            "pickupOtp" to pickupOtp,
            "deliveryOtp" to deliveryOtp,
            "liveDistanceRemainingKm" to 3.2,
            "liveEstimatedMinutes" to 18L,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        try {
            // Write order and optionally update stock
            val batch = db.batch()
            batch.set(orderDocRef, orderPayload)

            for (item in items) {
                if (item.productId.isNotBlank()) {
                    val prodRef = db.collection("products").document(item.productId)
                    batch.update(prodRef, "stockQuantity", FieldValue.increment(-item.quantity))
                }
            }

            batch.commit().await()

            return FirestoreOrder(
                id = orderDocRef.id,
                orderNumber = orderNumber,
                customerId = uid,
                customerName = customerName,
                customerPhone = customerPhone,
                deliveryAddress = deliveryAddress,
                sellerId = sellerId,
                shopId = shopId,
                shopName = shopName,
                items = items,
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                commissionPercent = commissionPercent,
                commissionAmount = commissionAmount,
                sellerPayout = sellerPayout,
                totalAmount = totalAmount,
                paymentMethod = paymentMethod,
                isPaid = (paymentMethod != "COD"),
                status = "PLACED",
                pickupOtp = pickupOtp,
                deliveryOtp = deliveryOtp
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, orderDocRef.path)
            throw e
        }
    }

    /**
     * Observes all orders placed by the current authenticated customer.
     */
    fun observeCustomerOrders(): Flow<List<FirestoreOrder>> = flow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            emit(emptyList())
            return@flow
        }
        val collection = db.collection("orders")
        emitAll(
            collection.whereEqualTo("customerId", uid)
                .snapshots()
                .map { snapshot -> snapshot.toObjects(FirestoreOrder::class.java) }
                .catch { e ->
                    if (e is Exception) handleFirestoreError(e, OperationType.LIST, collection.path)
                    emit(emptyList())
                }
        )
    }

    /**
     * Updates order status during delivery lifecycle.
     */
    suspend fun updateOrderStatus(orderId: String, newStatus: String) {
        val docRef = db.collection("orders").document(orderId)
        try {
            docRef.update(
                mapOf(
                    "status" to newStatus,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }
}
