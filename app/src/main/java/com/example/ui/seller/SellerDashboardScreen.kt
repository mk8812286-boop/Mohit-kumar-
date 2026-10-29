package com.example.ui.seller

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun SellerDashboardScreen(
    orders: List<Order>,
    products: List<PotteryProduct>,
    complaints: List<Complaint>,
    language: AppLanguage,
    onOpenAddProduct: () -> Unit,
    onAdvanceOrderStatus: (orderId: String, newStatus: OrderStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Orders, 1: Inventory, 2: Complaints & Earnings

    val todayOrders = orders
    val pendingPackingCount = orders.count { it.status == OrderStatus.PLACED || it.status == OrderStatus.SELLER_ACCEPTED || it.status == OrderStatus.PACKING }
    val readyForPickupCount = orders.count { it.status == OrderStatus.READY_FOR_PICKUP }
    val totalSellerEarnings = orders.filter { it.status != OrderStatus.CANCELLED }.sumOf { it.sellerPayout }
    val totalCommissionDeducted = orders.filter { it.status != OrderStatus.CANCELLED }.sumOf { it.commissionAmount }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
            .testTag("seller_dashboard_screen")
    ) {
        // Seller Shop Banner Header
        Surface(
            color = DarkChocolate,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Terracotta)
                                .border(1.5.dp, AntiqueGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = WarmCream,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.HINDI) "रामेश्वर प्रजापति पॉटरी वर्क्स" else "Rameshwar Prajapati Pottery",
                                color = WarmCream,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                fontSize = 15.sp
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) "सत्यापित कुम्हार विक्रेता • सांगानेर" else "Verified Potter Artisan • Sanganer",
                                color = AntiqueGoldLight,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Add Product Quick Button
                    Button(
                        onClick = onOpenAddProduct,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("seller_add_product_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "नया बर्तन" else "Add Pottery",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Cards (Daily Sales, Earnings, Pending)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Total Earnings
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF381F12),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "कुल कमाई (नेट)" else "Net Earnings",
                                color = AntiqueGoldLight,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "₹${totalSellerEarnings.toInt()}",
                                color = WarmCream,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) "10% कमीशन कटकर" else "Post 10% platform fee",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 8.sp
                            )
                        }
                    }

                    // Orders to pack
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF381F12),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "पैकिंग बाकी" else "Pending Packing",
                                color = AntiqueGoldLight,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "$pendingPackingCount",
                                color = if (pendingPackingCount > 0) Color(0xFFFFB74D) else WarmCream,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) "तुरंत पैक करें" else "Pack ASAP",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 8.sp
                            )
                        }
                    }

                    // Ready for pickup
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF381F12),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "पिकअप हेतु तैयार" else "Ready Pickup",
                                color = AntiqueGoldLight,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "$readyForPickupCount",
                                color = WarmCream,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) "राइडर आ रहा है" else "Rider on way",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 8.sp
                            )
                        }
                    }
                }
            }
        }

        // Tabs: 0: Orders, 1: Stock, 2: Earnings & Complaints
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Terracotta
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = if (language == AppLanguage.HINDI) "ऑर्डर प्रबंधन" else "Manage Orders",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = if (language == AppLanguage.HINDI) "बर्तन व स्टॉक (${products.size})" else "Stock (${products.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = {
                    Text(
                        text = if (language == AppLanguage.HINDI) "कमाई व शिकायतें" else "Payouts & Complaints",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // Orders Management
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(todayOrders) { order ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.2.dp, CardBorderGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "ऑर्डर #${order.orderNumber}",
                                            color = DarkChocolate,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "ग्राहक: ${order.customerName}",
                                            color = MutedText,
                                            fontSize = 11.sp
                                        )
                                    }

                                    // Payout to potter
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "कुम्हार राशि: ₹${order.sellerPayout.toInt()}",
                                            color = ForestGreen,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "(10% कमीशन कटकर)",
                                            color = MutedText,
                                            fontSize = 9.sp
                                        )
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBeige)

                                // Order items
                                order.items.forEach { item ->
                                    Text(
                                        text = "• ${item.product.nameHi} (${item.selectedSize.sizeNameEn}) x ${item.quantity}",
                                        color = DarkChocolate,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Status action button for potter
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Terracotta.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "स्थिति: ${order.status.titleHi}",
                                            color = TerracottaDark,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }

                                    when (order.status) {
                                        OrderStatus.PLACED -> {
                                            Button(
                                                onClick = { onAdvanceOrderStatus(order.id, OrderStatus.SELLER_ACCEPTED) },
                                                shape = RoundedCornerShape(6.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text("स्वीकार करें (Accept)", fontSize = 11.sp)
                                            }
                                        }
                                        OrderStatus.SELLER_ACCEPTED -> {
                                            Button(
                                                onClick = { onAdvanceOrderStatus(order.id, OrderStatus.PACKING) },
                                                shape = RoundedCornerShape(6.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text("पैकिंग शुरू करें", fontSize = 11.sp)
                                            }
                                        }
                                        OrderStatus.PACKING -> {
                                            Button(
                                                onClick = { onAdvanceOrderStatus(order.id, OrderStatus.READY_FOR_PICKUP) },
                                                shape = RoundedCornerShape(6.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = AntiqueGold),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text("पिकअप तैयार मार्क करें", fontSize = 11.sp, color = DarkChocolate)
                                            }
                                        }
                                        OrderStatus.READY_FOR_PICKUP -> {
                                            Text(
                                                text = "पिकअप OTP: ${order.pickupOtp}",
                                                color = DarkChocolate,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                        else -> {
                                            Text(
                                                text = "डिलीवरी साथी को सौंप दिया",
                                                color = ForestGreen,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Inventory Stock
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(products) { prod ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = prod.nameHi,
                                        color = DarkChocolate,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "श्रेणी: ${prod.category.nameHi} • कीमत: ₹${prod.basePrice.toInt()}",
                                        color = MutedText,
                                        fontSize = 11.sp
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (prod.stockQuantity > 0) ForestGreenLight else ErrorRedBg
                                ) {
                                    Text(
                                        text = if (prod.stockQuantity > 0) "स्टॉक: ${prod.stockQuantity}" else "आउट ऑफ स्टॉक",
                                        color = if (prod.stockQuantity > 0) ForestGreen else ErrorRed,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Earnings & Complaints
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Commission Breakdown Card (Requirement 9)
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                            border = androidx.compose.foundation.BorderStroke(1.2.dp, AntiqueGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) "कमीशन व भुगतान नियम (10% Multi-Vendor System)" else "10% Commission Structure",
                                    color = DarkChocolate,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Serif
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = if (language == AppLanguage.HINDI)
                                        "• बर्तन बिक्री मूल्य: ₹1,000\n• ऐप प्लेटफॉर्म कमीशन (10%): -₹100\n• कुम्हार बैंक खाते में जमा: ₹900\n(दैनिक ऑटो सेटलमेंट सीधे UPI / बैंक में)"
                                    else
                                        "• Pottery Sale: ₹1,000\n• Platform Commission (10%): -₹100\n• Potter Bank Payout: ₹900\n(Daily automated settlements to registered UPI)",
                                    color = CharcoalText,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // Complaints against potter's shop
                    item {
                        Text(
                            text = if (language == AppLanguage.HINDI) "ग्राहक शिकायतें (Customer Complaints - ${complaints.size}):" else "Customer Complaints (${complaints.size}):",
                            color = DarkChocolate,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    items(complaints) { comp ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = comp.productName,
                                        color = DarkChocolate,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = ErrorRedBg
                                    ) {
                                        Text(
                                            text = comp.status,
                                            color = ErrorRed,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "समस्या: ${comp.issueType}",
                                    color = ErrorRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = comp.description,
                                    color = MutedText,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
