package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.PotteryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen {
    // Customer
    CUSTOMER_HOME,
    CUSTOMER_NEARBY,
    CUSTOMER_CATEGORIES,
    CUSTOMER_PRODUCT_DETAIL,
    CUSTOMER_CART,
    CUSTOMER_ORDERS,
    CUSTOMER_LIVE_TRACKING,
    CUSTOMER_WISHLIST,
    
    // Seller
    SELLER_DASHBOARD,
    SELLER_ADD_PRODUCT,
    SELLER_ORDERS,
    SELLER_MY_SHOP,
    
    // Delivery
    DELIVERY_DASHBOARD,
    DELIVERY_ACTIVE_ROUTE,
    
    // Admin
    ADMIN_DASHBOARD,
    ADMIN_POTTERS,
    ADMIN_ORDERS,
    ADMIN_COMPLAINTS
}

class PotteryViewModel(
    val repository: PotteryRepository = PotteryRepository()
) : ViewModel() {

    val currentRole: StateFlow<UserRole> = repository.currentRole
    val language: StateFlow<AppLanguage> = repository.language
    val selectedDistanceKm: StateFlow<Int> = repository.selectedDistanceKm
    val selectedCategory: StateFlow<PotteryCategory> = repository.selectedCategory
    val searchQuery: StateFlow<String> = repository.searchQuery
    val customerLocation: StateFlow<String> = repository.customerLocation
    val adminCommissionPercent: StateFlow<Double> = repository.adminCommissionPercent

    val products: StateFlow<List<PotteryProduct>> = repository.products
    val shops: StateFlow<List<PotteryShop>> = repository.shops
    val cartItems: StateFlow<List<OrderItem>> = repository.cartItems
    val wishlistIds: StateFlow<Set<String>> = repository.wishlistIds
    val favoriteShopIds: StateFlow<Set<String>> = repository.favoriteShopIds
    val orders: StateFlow<List<Order>> = repository.orders
    val complaints: StateFlow<List<Complaint>> = repository.complaints
    val customRequests: StateFlow<List<CustomOrderRequest>> = repository.customRequests
    val selectedProduct: StateFlow<PotteryProduct?> = repository.selectedProduct
    val selectedTrackingOrder: StateFlow<Order?> = repository.selectedTrackingOrder

    private val _currentScreen = MutableStateFlow(AppScreen.CUSTOMER_HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Dialog & Flow States
    private val _showRoleSwitcher = MutableStateFlow(false)
    val showRoleSwitcher: StateFlow<Boolean> = _showRoleSwitcher.asStateFlow()

    private val _showComplaintDialog = MutableStateFlow(false)
    val showComplaintDialog: StateFlow<Boolean> = _showComplaintDialog.asStateFlow()

    private val _complaintTargetOrder = MutableStateFlow<Order?>(null)
    val complaintTargetOrder: StateFlow<Order?> = _complaintTargetOrder.asStateFlow()

    private val _showCustomOrderDialog = MutableStateFlow(false)
    val showCustomOrderDialog: StateFlow<Boolean> = _showCustomOrderDialog.asStateFlow()

    private val _showLoginDialog = MutableStateFlow(false)
    val showLoginDialog: StateFlow<Boolean> = _showLoginDialog.asStateFlow()

    private val _userPhoneNumber = MutableStateFlow("+91 98290 12345")
    val userPhoneNumber: StateFlow<String> = _userPhoneNumber.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _voiceSearchListening = MutableStateFlow(false)
    val voiceSearchListening: StateFlow<Boolean> = _voiceSearchListening.asStateFlow()

    // Filtered Products
    val filteredProducts: StateFlow<List<PotteryProduct>> = combine(
        products,
        selectedCategory,
        searchQuery,
        selectedDistanceKm,
        shops
    ) { allProducts, category, query, distanceKm, allShops ->
        val allowedShopIds = allShops.filter { it.distanceKm <= distanceKm }.map { it.id }.toSet()
        allProducts.filter { product ->
            val matchesCategory = category == PotteryCategory.ALL || product.category == category
            val matchesQuery = query.isBlank() ||
                    product.nameHi.contains(query, ignoreCase = true) ||
                    product.nameEn.contains(query, ignoreCase = true) ||
                    product.descriptionHi.contains(query, ignoreCase = true) ||
                    product.descriptionEn.contains(query, ignoreCase = true) ||
                    product.potterNameHi.contains(query, ignoreCase = true) ||
                    product.potterNameEn.contains(query, ignoreCase = true)
            val matchesDistance = allowedShopIds.contains(product.shopId) || distanceKm >= 20
            matchesCategory && matchesQuery && matchesDistance
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Shops
    val filteredShops: StateFlow<List<PotteryShop>> = combine(
        shops,
        selectedDistanceKm,
        searchQuery
    ) { allShops, distanceKm, query ->
        allShops.filter { shop ->
            val matchesDistance = shop.distanceKm <= distanceKm || distanceKm >= 20
            val matchesQuery = query.isBlank() ||
                    shop.nameHi.contains(query, ignoreCase = true) ||
                    shop.nameEn.contains(query, ignoreCase = true) ||
                    shop.potterNameHi.contains(query, ignoreCase = true) ||
                    shop.villageCity.contains(query, ignoreCase = true)
            matchesDistance && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation
    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun openProductDetail(product: PotteryProduct) {
        repository.selectProduct(product)
        _currentScreen.value = AppScreen.CUSTOMER_PRODUCT_DETAIL
    }

    fun openLiveTracking(order: Order) {
        repository.selectTrackingOrder(order)
        if (currentRole.value == UserRole.DELIVERY) {
            _currentScreen.value = AppScreen.DELIVERY_ACTIVE_ROUTE
        } else {
            _currentScreen.value = AppScreen.CUSTOMER_LIVE_TRACKING
        }
    }

    // Role Switching
    fun switchRole(role: UserRole) {
        repository.setRole(role)
        _showRoleSwitcher.value = false
        _currentScreen.value = when (role) {
            UserRole.CUSTOMER -> AppScreen.CUSTOMER_HOME
            UserRole.SELLER -> AppScreen.SELLER_DASHBOARD
            UserRole.DELIVERY -> AppScreen.DELIVERY_DASHBOARD
            UserRole.ADMIN -> AppScreen.ADMIN_DASHBOARD
        }
    }

    fun setShowRoleSwitcher(show: Boolean) {
        _showRoleSwitcher.value = show
    }

    fun toggleLanguage() {
        repository.toggleLanguage()
    }

    fun setDistanceFilter(km: Int) {
        repository.setDistanceFilter(km)
    }

    fun setCategory(category: PotteryCategory) {
        repository.setCategory(category)
    }

    fun setSearchQuery(query: String) {
        repository.setSearchQuery(query)
    }

    fun simulateVoiceSearch() {
        viewModelScope.launch {
            _voiceSearchListening.value = true
            kotlinx.coroutines.delay(1200)
            repository.setSearchQuery("मटका")
            _voiceSearchListening.value = false
        }
    }

    fun toggleWishlist(productId: String) {
        repository.toggleWishlist(productId)
    }

    fun toggleFavoriteShop(shopId: String) {
        repository.toggleFavoriteShop(shopId)
    }

    // Cart Actions
    fun addToCart(product: PotteryProduct, size: ProductVariantSize, color: ClayColor, quantity: Int = 1) {
        repository.addToCart(product, size, color, quantity)
    }

    fun updateCartQuantity(index: Int, qty: Int) {
        repository.updateCartQuantity(index, qty)
    }

    fun placeOrder(
        customerName: String,
        phone: String,
        address: String,
        paymentMethod: PaymentMethod
    ): Order {
        val newOrder = repository.placeOrder(customerName, phone, address, paymentMethod)
        _currentScreen.value = AppScreen.CUSTOMER_LIVE_TRACKING
        return newOrder
    }

    // Complaints
    fun openComplaintDialog(order: Order) {
        _complaintTargetOrder.value = order
        _showComplaintDialog.value = true
    }

    fun closeComplaintDialog() {
        _showComplaintDialog.value = false
        _complaintTargetOrder.value = null
    }

    fun submitComplaint(
        orderNumber: String,
        productName: String,
        issueType: String,
        description: String,
        resolutionType: String,
        potterShopName: String,
        refundAmount: Double
    ) {
        repository.submitComplaint(
            orderNumber,
            productName,
            issueType,
            description,
            resolutionType,
            potterShopName,
            refundAmount
        )
        closeComplaintDialog()
    }

    // Custom Order
    fun openCustomOrderDialog() {
        _showCustomOrderDialog.value = true
    }

    fun closeCustomOrderDialog() {
        _showCustomOrderDialog.value = false
    }

    fun submitCustomOrder(
        customerName: String,
        customerPhone: String,
        shopName: String,
        description: String,
        dimensions: String,
        budget: Double
    ) {
        repository.submitCustomOrder(customerName, customerPhone, shopName, description, dimensions, budget)
        closeCustomOrderDialog()
    }

    // Order status updates (for seller & delivery flows)
    fun advanceOrderStatus(orderId: String, newStatus: OrderStatus) {
        repository.updateOrderStatus(orderId, newStatus)
    }

    fun confirmPackingPhoto(orderId: String) {
        repository.confirmPackingPhoto(orderId)
    }

    fun addProduct(product: PotteryProduct) {
        repository.addProduct(product)
        _currentScreen.value = AppScreen.SELLER_DASHBOARD
    }

    fun registerShop(shop: PotteryShop) {
        repository.registerShop(shop)
        _currentScreen.value = AppScreen.SELLER_DASHBOARD
    }

    fun setAdminCommission(percent: Double) {
        repository.setAdminCommission(percent)
    }

    fun toggleShopApproval(shopId: String) {
        repository.toggleShopApproval(shopId)
    }

    fun updateComplaintStatus(complaintId: String, status: String) {
        repository.updateComplaintStatus(complaintId, status)
    }

    fun setShowLoginDialog(show: Boolean) {
        _showLoginDialog.value = show
    }

    fun loginWithOtp(phone: String, otp: String) {
        _userPhoneNumber.value = phone
        _isLoggedIn.value = true
        _showLoginDialog.value = false
    }
}
