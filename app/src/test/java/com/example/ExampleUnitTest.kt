package com.example

import com.example.data.model.*
import com.example.data.model.firestore.FirestoreOrder
import com.example.data.model.firestore.FirestoreOrderItem
import com.example.data.model.firestore.FirestoreProduct
import com.example.data.model.firestore.FirestoreShop
import com.example.data.repository.PotteryRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var repository: PotteryRepository

    @Before
    fun setup() {
        repository = PotteryRepository()
    }

    @Test
    fun testInitialProductsAndShops() {
        val products = repository.products.value
        val shops = repository.shops.value
        assertTrue("Products should be populated", products.isNotEmpty())
        assertTrue("Shops should be populated", shops.isNotEmpty())
        assertEquals("Initial commission should be 10%", 10.0, repository.adminCommissionPercent.value, 0.01)
    }

    @Test
    fun testAddToCartAndCommissionCalculation() {
        val product = repository.products.value.first()
        val size = product.sizes.first()
        val color = product.colors.first()

        repository.addToCart(product, size, color, 2)
        val cartItems = repository.cartItems.value
        assertEquals(1, cartItems.size)
        assertEquals(2, cartItems[0].quantity)

        val order = repository.placeOrder(
            customerName = "राम कुमार",
            phone = "+91 98290 00000",
            address = "जयपुर",
            paymentMethod = PaymentMethod.UPI
        )

        assertEquals(0, repository.cartItems.value.size)
        assertTrue(order.orderNumber.startsWith("PK-"))
        assertEquals(order.subtotal * 0.10, order.commissionAmount, 0.1)
        assertEquals(order.subtotal * 0.90, order.sellerPayout, 0.1)
    }

    @Test
    fun testOrderStatusAdvancement() {
        val initialOrder = repository.orders.value.first()
        val initialId = initialOrder.id

        repository.updateOrderStatus(initialId, OrderStatus.DELIVERED)
        val updated = repository.orders.value.first { it.id == initialId }
        assertEquals(OrderStatus.DELIVERED, updated.status)
        assertTrue(updated.isPaid)
        assertEquals(0.0, updated.liveDistanceRemainingKm, 0.01)
    }

    @Test
    fun testFirestoreShopDataModel() {
        val shop = FirestoreShop(
            id = "shop_99",
            ownerId = "artisan_1",
            nameHi = "माटी शिल्प",
            nameEn = "Maati Craft",
            potterNameHi = "सुरेश प्रजापति",
            phone = "+91 98290 11223",
            fullAddress = "जयपुर, राजस्थान",
            upiId = "suresh@upi",
            isApproved = true,
            distanceKm = 3.5
        )

        assertEquals("shop_99", shop.id)
        assertEquals("artisan_1", shop.ownerId)
        assertTrue(shop.isApproved)
        assertEquals(3.5, shop.distanceKm, 0.01)
    }

    @Test
    fun testFirestoreProductAndOrderDataModels() {
        val product = FirestoreProduct(
            id = "prod_10",
            shopId = "shop_99",
            ownerId = "artisan_1",
            nameHi = "शीतल जल मटका",
            nameEn = "Cooling Water Matka",
            basePrice = 400.0,
            stockQuantity = 25
        )

        val orderItem = FirestoreOrderItem(
            productId = product.id,
            productNameHi = product.nameHi,
            productNameEn = product.nameEn,
            quantity = 2,
            unitPrice = 400.0,
            totalPrice = 800.0,
            shopId = "shop_99"
        )

        val subtotal = orderItem.unitPrice * orderItem.quantity
        val commissionRate = 10.0
        val commissionAmount = subtotal * (commissionRate / 100.0)
        val sellerPayout = subtotal - commissionAmount
        val deliveryFee = 0.0 // Free delivery over 500

        val order = FirestoreOrder(
            id = "ord_55",
            orderNumber = "PK-7788",
            customerId = "user_123",
            customerName = "राजेश",
            sellerId = "artisan_1",
            shopId = "shop_99",
            items = listOf(orderItem),
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            commissionPercent = commissionRate,
            commissionAmount = commissionAmount,
            sellerPayout = sellerPayout,
            totalAmount = subtotal + deliveryFee,
            status = "PLACED"
        )

        assertEquals(800.0, order.subtotal, 0.01)
        assertEquals(80.0, order.commissionAmount, 0.01)
        assertEquals(720.0, order.sellerPayout, 0.01)
        assertEquals(800.0, order.totalAmount, 0.01)
        assertEquals("PLACED", order.status)
    }
}
