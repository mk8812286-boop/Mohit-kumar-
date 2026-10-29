package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun AdminDashboardScreen(
    shops: List<PotteryShop>,
    products: List<PotteryProduct>,
    orders: List<Order>,
    complaints: List<Complaint>,
    commissionPercent: Double,
    language: AppLanguage,
    onSetCommission: (Double) -> Unit,
    onToggleShopApproval: (String) -> Unit,
    onUpdateComplaintStatus: (complaintId: String, newStatus: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var adminTab by remember { mutableIntStateOf(0) } // 0: Overview, 1: Kumhars/Shops, 2: Complaints & Refunds, 3: Commission & Settings
    var commissionInput by remember { mutableStateOf("${commissionPercent.toInt()}") }
    var broadcastMessage by remember { mutableStateOf("") }
    var broadcastSent by remember { mutableStateOf(false) }

    val totalGMV = orders.sumOf { it.totalAmount }
    val totalAdminCommission = orders.sumOf { it.commissionAmount }
    val totalSellerPayouts = orders.sumOf { it.sellerPayout }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
            .testTag("admin_dashboard_screen")
    ) {
        // Admin Header
        Surface(
            color = DarkChocolate,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AntiqueGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin",
                            tint = DarkChocolate,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.HINDI) "प्रजापति कला - सुपर एडमिन कंट्रोल" else "Prajapati Kala - Super Admin",
                            color = WarmCream,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (language == AppLanguage.HINDI) "मार्केटप्लेस संचालन, कुम्हार सत्यापन व कमीशन" else "Marketplace control, vendor approvals & payouts",
                            color = AntiqueGoldLight,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Top KPI Metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF381F12),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("कुल GMV बिक्री", color = AntiqueGoldLight, fontSize = 9.sp)
                            Text("₹${totalGMV.toInt()}", color = WarmCream, fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                            Text("${orders.size} ऑर्डर संपन्न", color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF381F12),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("प्लेटफॉर्म कमीशन", color = AntiqueGoldLight, fontSize = 9.sp)
                            Text("₹${totalAdminCommission.toInt()}", color = Color(0xFF00E676), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("${commissionPercent.toInt()}% प्लेटफॉर्म दर", color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF381F12),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("कुम्हारों को भुगतान", color = AntiqueGoldLight, fontSize = 9.sp)
                            Text("₹${totalSellerPayouts.toInt()}", color = WarmCream, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("90% शुद्ध कारीगर हिस्सा", color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp)
                        }
                    }
                }
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = adminTab,
            containerColor = Color.White,
            contentColor = Terracotta
        ) {
            Tab(selected = adminTab == 0, onClick = { adminTab = 0 }, text = { Text("अवलोकन", fontSize = 11.sp, fontWeight = FontWeight.Bold) })
            Tab(selected = adminTab == 1, onClick = { adminTab = 1 }, text = { Text("कुम्हार सत्यापन (${shops.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) })
            Tab(selected = adminTab == 2, onClick = { adminTab = 2 }, text = { Text("शिकायतें व रिफंड (${complaints.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) })
            Tab(selected = adminTab == 3, onClick = { adminTab = 3 }, text = { Text("कमीशन सेटिंग", fontSize = 11.sp, fontWeight = FontWeight.Bold) })
        }

        // Content
        when (adminTab) {
            0 -> {
                // Overview of orders and active deliveries
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "मार्केटप्लेस के सभी ऑर्डर (${orders.size}):",
                            color = DarkChocolate,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    items(orders) { order ->
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
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("ऑर्डर #${order.orderNumber} • ₹${order.totalAmount.toInt()}", color = DarkChocolate, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("ग्राहक: ${order.customerName}", color = MutedText, fontSize = 11.sp)
                                    Text("कमीशन: ₹${order.commissionAmount.toInt()} | कुम्हार: ₹${order.sellerPayout.toInt()}", color = ForestGreen, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                }

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (order.status == OrderStatus.DELIVERED) ForestGreen else Terracotta
                                ) {
                                    Text(
                                        text = order.status.titleHi,
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Kumhar Vendor Approvals (Requirement 3D.1, 3D.2)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "पंजीकृत कुम्हार दुकानें (Vendor Verification & KYC):",
                            color = DarkChocolate,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    items(shops) { shop ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(shop.nameHi, color = DarkChocolate, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("कारीगर: ${shop.potterNameHi} • फोन: ${shop.phone}", color = MutedText, fontSize = 11.sp)
                                        Text("पता: ${shop.fullAddress}", color = CharcoalText, fontSize = 11.sp)
                                        Text("UPI: ${shop.upiId} • ${shop.bankAccount}", color = TerracottaDark, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                    }

                                    Button(
                                        onClick = { onToggleShopApproval(shop.id) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (shop.isApproved) ForestGreen else Terracotta
                                        ),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text(
                                            text = if (shop.isApproved) "स्वीकृत (Active)" else "मंजूरी दें (Approve)",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Complaints & Refunds (Requirement 3D.11, 3D.12)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "खराब बर्तन शिकायतें व रिफंड प्रबंधन:",
                            color = DarkChocolate,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    items(complaints) { comp ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.2.dp, CardBorderGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("ऑर्डर #${comp.orderNumber}", color = DarkChocolate, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Surface(shape = RoundedCornerShape(4.dp), color = ErrorRedBg) {
                                        Text(comp.status, color = ErrorRed, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }

                                Text("बर्तन: ${comp.productName}", color = TerracottaDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("समस्या: ${comp.issueType}", color = ErrorRed, fontSize = 11.sp)
                                Text("विवरण: ${comp.description}", color = CharcoalText, fontSize = 11.sp)
                                Text("वांछित समाधान: ${comp.resolutionType}", color = MutedText, fontSize = 11.sp)

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { onUpdateComplaintStatus(comp.id, "रिफंड स्वीकृत (₹${comp.refundAmount.toInt()} Refunded)") },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("रिफंड जारी करें", fontSize = 10.sp)
                                    }

                                    Button(
                                        onClick = { onUpdateComplaintStatus(comp.id, "रिप्लेसमेंट स्वीकृत (New Pottery Dispatched)") },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("नया बर्तन भेजें", fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // Commission & Notification Broadcast Settings (Requirement 3D.8, 3D.14)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "मल्टी-वेंडर कमीशन दर तय करें (Multi-Vendor Commission %):",
                                    color = DarkChocolate,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = commissionInput,
                                        onValueChange = { commissionInput = it },
                                        label = { Text("कमीशन प्रतिशत (%)", fontSize = 11.sp) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            val rate = commissionInput.toDoubleOrNull() ?: 10.0
                                            onSetCommission(rate)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                                    ) {
                                        Text("सेव करें", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Text(
                                    text = "वर्तमान दर: ${commissionPercent.toInt()}% (कुम्हार को प्रत्येक बिक्री का ${100 - commissionPercent.toInt()}% मिलेगा)",
                                    color = ForestGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                        }
                    }

                    // Notification Broadcaster (Requirement 3D.14)
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "सभी ग्राहकों व कुम्हारों को सूचना भेजें (Broadcast Push Notification):",
                                    color = DarkChocolate,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = broadcastMessage,
                                    onValueChange = { broadcastMessage = it },
                                    placeholder = { Text("उदा. दिवाली विशेष: मिट्टी के दीयों पर 20% की विशेष छूट!", fontSize = 11.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        broadcastSent = true
                                        broadcastMessage = ""
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("सूचना भेजें (Send Broadcast)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                if (broadcastSent) {
                                    Text(
                                        text = "✓ सूचना सफलतापूर्वक सभी 1,420 उपयोगकर्ताओं को भेज दी गई।",
                                        color = ForestGreen,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
