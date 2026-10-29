package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.*
import com.example.ui.AppScreen
import com.example.ui.PotteryViewModel
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.components.*
import com.example.ui.customer.*
import com.example.ui.delivery.DeliveryDashboardScreen
import com.example.ui.seller.SellerAddProductScreen
import com.example.ui.seller.SellerDashboardScreen
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    private val viewModel: PotteryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PrajapatiKalaTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: PotteryViewModel) {
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val location by viewModel.customerLocation.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isVoiceSearching by viewModel.voiceSearchListening.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedDistanceKm by viewModel.selectedDistanceKm.collectAsStateWithLifecycle()
    val adminCommission by viewModel.adminCommissionPercent.collectAsStateWithLifecycle()

    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val filteredShops by viewModel.filteredShops.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()
    val favoriteShopIds by viewModel.favoriteShopIds.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val complaints by viewModel.complaints.collectAsStateWithLifecycle()
    val selectedProduct by viewModel.selectedProduct.collectAsStateWithLifecycle()
    val selectedTrackingOrder by viewModel.selectedTrackingOrder.collectAsStateWithLifecycle()

    val showRoleSwitcher by viewModel.showRoleSwitcher.collectAsStateWithLifecycle()
    val showComplaintDialog by viewModel.showComplaintDialog.collectAsStateWithLifecycle()
    val complaintTargetOrder by viewModel.complaintTargetOrder.collectAsStateWithLifecycle()
    val showCustomOrderDialog by viewModel.showCustomOrderDialog.collectAsStateWithLifecycle()

    // Handle back button
    BackHandler(enabled = currentScreen != AppScreen.CUSTOMER_HOME && currentScreen != AppScreen.SELLER_DASHBOARD && currentScreen != AppScreen.DELIVERY_DASHBOARD && currentScreen != AppScreen.ADMIN_DASHBOARD) {
        when (currentRole) {
            UserRole.CUSTOMER -> viewModel.navigateTo(AppScreen.CUSTOMER_HOME)
            UserRole.SELLER -> viewModel.navigateTo(AppScreen.SELLER_DASHBOARD)
            UserRole.DELIVERY -> viewModel.navigateTo(AppScreen.DELIVERY_DASHBOARD)
            UserRole.ADMIN -> viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WarmCream,
        topBar = {
            val showBack = currentScreen == AppScreen.CUSTOMER_PRODUCT_DETAIL ||
                    currentScreen == AppScreen.CUSTOMER_CART ||
                    currentScreen == AppScreen.CUSTOMER_LIVE_TRACKING ||
                    currentScreen == AppScreen.CUSTOMER_NEARBY ||
                    currentScreen == AppScreen.CUSTOMER_ORDERS ||
                    currentScreen == AppScreen.SELLER_ADD_PRODUCT

            PrajapatiTopBar(
                role = currentRole,
                language = language,
                cartCount = cartItems.sumOf { it.quantity },
                showBackButton = showBack,
                onBackClick = {
                    when (currentRole) {
                        UserRole.CUSTOMER -> viewModel.navigateTo(AppScreen.CUSTOMER_HOME)
                        UserRole.SELLER -> viewModel.navigateTo(AppScreen.SELLER_DASHBOARD)
                        UserRole.DELIVERY -> viewModel.navigateTo(AppScreen.DELIVERY_DASHBOARD)
                        UserRole.ADMIN -> viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD)
                    }
                },
                onRoleClick = { viewModel.setShowRoleSwitcher(true) },
                onLanguageClick = { viewModel.toggleLanguage() },
                onCartClick = { viewModel.navigateTo(AppScreen.CUSTOMER_CART) }
            )
        },
        bottomBar = {
            if (currentRole == UserRole.CUSTOMER && currentScreen != AppScreen.CUSTOMER_PRODUCT_DETAIL && currentScreen != AppScreen.CUSTOMER_CART) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 6.dp,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.testTag("customer_bottom_nav")
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.CUSTOMER_HOME,
                        onClick = { viewModel.navigateTo(AppScreen.CUSTOMER_HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = {
                            Text(
                                text = if (language == AppLanguage.HINDI) "मुख्य" else "Home",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DarkChocolate,
                            selectedTextColor = TerracottaDark,
                            indicatorColor = SoftBeige,
                            unselectedIconColor = MutedText,
                            unselectedTextColor = MutedText
                        )
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.CUSTOMER_NEARBY,
                        onClick = { viewModel.navigateTo(AppScreen.CUSTOMER_NEARBY) },
                        icon = { Icon(Icons.Default.NearMe, contentDescription = "Nearby") },
                        label = {
                            Text(
                                text = if (language == AppLanguage.HINDI) "कुम्हार" else "Nearby",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DarkChocolate,
                            selectedTextColor = TerracottaDark,
                            indicatorColor = SoftBeige,
                            unselectedIconColor = MutedText,
                            unselectedTextColor = MutedText
                        )
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.CUSTOMER_ORDERS,
                        onClick = { viewModel.navigateTo(AppScreen.CUSTOMER_ORDERS) },
                        icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Orders") },
                        label = {
                            Text(
                                text = if (language == AppLanguage.HINDI) "ऑर्डर" else "Orders",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DarkChocolate,
                            selectedTextColor = TerracottaDark,
                            indicatorColor = SoftBeige,
                            unselectedIconColor = MutedText,
                            unselectedTextColor = MutedText
                        )
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.CUSTOMER_LIVE_TRACKING,
                        onClick = {
                            orders.firstOrNull()?.let { viewModel.openLiveTracking(it) }
                        },
                        icon = { Icon(Icons.Default.TwoWheeler, contentDescription = "Tracking") },
                        label = {
                            Text(
                                text = if (language == AppLanguage.HINDI) "ट्रैकिंग" else "Tracking",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DarkChocolate,
                            selectedTextColor = TerracottaDark,
                            indicatorColor = SoftBeige,
                            unselectedIconColor = MutedText,
                            unselectedTextColor = MutedText
                        )
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.setShowRoleSwitcher(true) },
                        icon = { Icon(Icons.Default.SwitchAccount, contentDescription = "Switch Panel") },
                        label = {
                            Text(
                                text = if (language == AppLanguage.HINDI) "पैनल" else "Panels",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DarkChocolate,
                            selectedTextColor = TerracottaDark,
                            indicatorColor = SoftBeige,
                            unselectedIconColor = MutedText,
                            unselectedTextColor = MutedText
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRole) {
                UserRole.CUSTOMER -> {
                    when (currentScreen) {
                        AppScreen.CUSTOMER_HOME, AppScreen.CUSTOMER_CATEGORIES -> {
                            CustomerHomeScreen(
                                location = location,
                                searchQuery = searchQuery,
                                isVoiceSearching = isVoiceSearching,
                                selectedCategory = selectedCategory,
                                selectedDistanceKm = selectedDistanceKm,
                                products = filteredProducts,
                                shops = filteredShops,
                                wishlistIds = wishlistIds,
                                favoriteShopIds = favoriteShopIds,
                                language = language,
                                onSearchChange = { viewModel.setSearchQuery(it) },
                                onVoiceSearch = { viewModel.simulateVoiceSearch() },
                                onSelectCategory = { viewModel.setCategory(it) },
                                onSelectDistance = { viewModel.setDistanceFilter(it) },
                                onProductClick = { viewModel.openProductDetail(it) },
                                onAddToCart = { product ->
                                    val size = product.sizes.firstOrNull() ?: ProductVariantSize("मध्यम", "Medium", "5L", "24cm", "2kg")
                                    val color = product.colors.firstOrNull() ?: ClayColor("लाल गेरू", "Terracotta Red", 0xFFA65E3B)
                                    viewModel.addToCart(product, size, color, 1)
                                },
                                onToggleWishlist = { viewModel.toggleWishlist(it) },
                                onShopClick = { shop ->
                                    viewModel.setSearchQuery(shop.nameHi)
                                },
                                onToggleFavoriteShop = { viewModel.toggleFavoriteShop(it) },
                                onViewAllShops = { viewModel.navigateTo(AppScreen.CUSTOMER_NEARBY) },
                                onOpenCustomOrder = { viewModel.openCustomOrderDialog() }
                            )
                        }

                        AppScreen.CUSTOMER_PRODUCT_DETAIL -> {
                            selectedProduct?.let { prod ->
                                PotteryDetailScreen(
                                    product = prod,
                                    language = language,
                                    isWishlisted = wishlistIds.contains(prod.id),
                                    onAddToCart = { size, color, qty ->
                                        viewModel.addToCart(prod, size, color, qty)
                                    },
                                    onBuyNow = { size, color, qty ->
                                        viewModel.addToCart(prod, size, color, qty)
                                        viewModel.navigateTo(AppScreen.CUSTOMER_CART)
                                    },
                                    onToggleWishlist = { viewModel.toggleWishlist(prod.id) },
                                    onOpenCustomRequest = { viewModel.openCustomOrderDialog() }
                                )
                            }
                        }

                        AppScreen.CUSTOMER_NEARBY -> {
                            NearbyShopsScreen(
                                shops = filteredShops,
                                selectedDistanceKm = selectedDistanceKm,
                                favoriteShopIds = favoriteShopIds,
                                language = language,
                                onSelectDistance = { viewModel.setDistanceFilter(it) },
                                onShopClick = { shop ->
                                    viewModel.setSearchQuery(shop.nameHi)
                                    viewModel.navigateTo(AppScreen.CUSTOMER_HOME)
                                },
                                onToggleFavorite = { viewModel.toggleFavoriteShop(it) }
                            )
                        }

                        AppScreen.CUSTOMER_CART -> {
                            CartScreen(
                                cartItems = cartItems,
                                customerLocation = location,
                                language = language,
                                onUpdateQuantity = { idx, qty -> viewModel.updateCartQuantity(idx, qty) },
                                onPlaceOrder = { name, phone, address, pay ->
                                    viewModel.placeOrder(name, phone, address, pay)
                                },
                                onBrowseProducts = { viewModel.navigateTo(AppScreen.CUSTOMER_HOME) }
                            )
                        }

                        AppScreen.CUSTOMER_ORDERS -> {
                            CustomerOrdersScreen(
                                orders = orders,
                                language = language,
                                onTrackOrder = { viewModel.openLiveTracking(it) },
                                onReportProblem = { viewModel.openComplaintDialog(it) },
                                onReorder = { order ->
                                    order.items.forEach { item ->
                                        viewModel.addToCart(item.product, item.selectedSize, item.selectedColor, item.quantity)
                                    }
                                    viewModel.navigateTo(AppScreen.CUSTOMER_CART)
                                }
                            )
                        }

                        AppScreen.CUSTOMER_LIVE_TRACKING -> {
                            selectedTrackingOrder?.let { order ->
                                LiveOrderTrackingView(
                                    order = order,
                                    language = language,
                                    onAdvanceStatus = { nextStatus ->
                                        viewModel.advanceOrderStatus(order.id, nextStatus)
                                    },
                                    onReportProblem = {
                                        viewModel.openComplaintDialog(order)
                                    }
                                )
                            }
                        }

                        else -> {
                            viewModel.navigateTo(AppScreen.CUSTOMER_HOME)
                        }
                    }
                }

                UserRole.SELLER -> {
                    when (currentScreen) {
                        AppScreen.SELLER_ADD_PRODUCT -> {
                            SellerAddProductScreen(
                                language = language,
                                onSaveProduct = { viewModel.addProduct(it) },
                                onCancel = { viewModel.navigateTo(AppScreen.SELLER_DASHBOARD) }
                            )
                        }
                        else -> {
                            SellerDashboardScreen(
                                orders = orders,
                                products = filteredProducts,
                                complaints = complaints,
                                language = language,
                                onOpenAddProduct = { viewModel.navigateTo(AppScreen.SELLER_ADD_PRODUCT) },
                                onAdvanceOrderStatus = { id, st -> viewModel.advanceOrderStatus(id, st) }
                            )
                        }
                    }
                }

                UserRole.DELIVERY -> {
                    DeliveryDashboardScreen(
                        orders = orders,
                        language = language,
                        onOpenTrip = { viewModel.openLiveTracking(it) },
                        onAdvanceStatus = { id, st -> viewModel.advanceOrderStatus(id, st) },
                        onConfirmPackingPhoto = { viewModel.confirmPackingPhoto(it) }
                    )
                }

                UserRole.ADMIN -> {
                    AdminDashboardScreen(
                        shops = filteredShops,
                        products = filteredProducts,
                        orders = orders,
                        complaints = complaints,
                        commissionPercent = adminCommission,
                        language = language,
                        onSetCommission = { viewModel.setAdminCommission(it) },
                        onToggleShopApproval = { viewModel.toggleShopApproval(it) },
                        onUpdateComplaintStatus = { id, st -> viewModel.updateComplaintStatus(id, st) }
                    )
                }
            }
        }
    }

    // Role Switcher Dialog
    if (showRoleSwitcher) {
        RoleSwitcherDialog(
            currentRole = currentRole,
            language = language,
            onRoleSelected = { viewModel.switchRole(it) },
            onDismiss = { viewModel.setShowRoleSwitcher(false) }
        )
    }

    // Defective Pottery Complaint Dialog
    if (showComplaintDialog) {
        complaintTargetOrder?.let { targetOrder ->
            ComplaintDialog(
                order = targetOrder,
                language = language,
                onSubmitComplaint = { ordNum, prod, issue, desc, res, shop, refAmount ->
                    viewModel.submitComplaint(ordNum, prod, issue, desc, res, shop, refAmount)
                },
                onDismiss = { viewModel.closeComplaintDialog() }
            )
        }
    }

    // Custom Order Request Dialog
    if (showCustomOrderDialog) {
        CustomOrderDialog(
            shops = filteredShops,
            language = language,
            onSubmitCustomOrder = { name, phone, shop, desc, dim, bud ->
                viewModel.submitCustomOrder(name, phone, shop, desc, dim, bud)
            },
            onDismiss = { viewModel.closeCustomOrderDialog() }
        )
    }
}
