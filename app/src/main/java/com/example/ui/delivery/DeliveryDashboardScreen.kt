package com.example.ui.delivery

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
import com.example.data.model.AppLanguage
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.ui.theme.*

@Composable
fun DeliveryDashboardScreen(
    orders: List<Order>,
    language: AppLanguage,
    onOpenTrip: (Order) -> Unit,
    onAdvanceStatus: (orderId: String, newStatus: OrderStatus) -> Unit,
    onConfirmPackingPhoto: (orderId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var riderActive by remember { mutableStateOf(true) }
    var enteredPickupOtp by remember { mutableStateOf("") }
    var enteredDeliveryOtp by remember { mutableStateOf("") }

    val activePickups = orders.filter { it.status == OrderStatus.READY_FOR_PICKUP || it.status == OrderStatus.PARTNER_ASSIGNED }
    val outForDelivery = orders.filter { it.status == OrderStatus.PICKED_UP || it.status == OrderStatus.OUT_FOR_DELIVERY }
    val completedDeliveries = orders.filter { it.status == OrderStatus.DELIVERED }

    val totalEarnings = completedDeliveries.size * 50.0 + (if (completedDeliveries.isNotEmpty()) 120.0 else 0.0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
            .testTag("delivery_dashboard_screen")
    ) {
        // Rider Header Bar
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
                                .background(ForestGreen)
                                .border(1.5.dp, AntiqueGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TwoWheeler,
                                contentDescription = "Rider",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "सुरेश कुमार (Suresh Kumar)",
                                color = WarmCream,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                fontSize = 15.sp
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) "सत्यापित डिलीवरी पार्टनर • RJ-14 AB 4912" else "Verified Partner • RJ-14 AB 4912",
                                color = AntiqueGoldLight,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Online/Offline Toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(if (riderActive) ForestGreen else Color.Gray, RoundedCornerShape(20.dp))
                            .clickable { riderActive = !riderActive }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (riderActive) "ऑनलाइन" else "ऑफलाइन",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rider Earnings Card (Requirement 3C.13)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF2E190E),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("आज की कमाई (Earnings)", color = AntiqueGoldLight, fontSize = 10.sp)
                            Text("₹${totalEarnings.toInt()}", color = WarmCream, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("प्रति ऑर्डर ₹50 + बोनस", color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF2E190E),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("पिकअप कार्य (Active)", color = AntiqueGoldLight, fontSize = 10.sp)
                            Text("${activePickups.size + outForDelivery.size}", color = Color(0xFFFFB74D), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("सुरक्षित मिट्टी पार्सल", color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF2E190E),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("सफल डिलीवरी", color = AntiqueGoldLight, fontSize = 10.sp)
                            Text("${completedDeliveries.size}", color = WarmCream, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("100% सुरक्षित हैंडओवर", color = Color.White.copy(alpha = 0.6f), fontSize = 8.sp)
                        }
                    }
                }
            }
        }

        // Active Deliveries List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = if (language == AppLanguage.HINDI) "सक्रिय डिलीवरी असाइनमेंट्स (Active Trips)" else "Active Assignments",
                    color = DarkChocolate,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }

            // List pending pickups and out-for-deliveries
            items(orders) { order ->
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
                                    text = "डिलीवरी कमाई: ₹50.00",
                                    color = ForestGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (order.status == OrderStatus.DELIVERED) ForestGreen else Terracotta
                            ) {
                                Text(
                                    text = order.status.titleHi,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBeige)

                        // Pickup from potter info
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Storefront, contentDescription = null, tint = Terracotta, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "पिकअप: रामेश्वर प्रजापति पॉटरी वर्क्स, सांगानेर (2.4 KM)",
                                color = DarkChocolate,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Delivery to customer info
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "डिलीवरी: ${order.customerName} (${order.deliveryAddress})",
                                color = CharcoalText,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Packing photo verification requirement (Requirement 3C.6)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = WarmCreamSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (order.packingPhotoTaken) Icons.Default.CheckCircle else Icons.Default.CameraAlt,
                                        contentDescription = null,
                                        tint = if (order.packingPhotoTaken) ForestGreen else Terracotta,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (order.packingPhotoTaken) "पैकिंग फोटो सत्यापित (Verified)" else "पिकअप पूर्व पैकिंग फोटो आवश्यक",
                                        color = DarkChocolate,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (!order.packingPhotoTaken) {
                                    Button(
                                        onClick = { onConfirmPackingPhoto(order.id) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        modifier = Modifier.height(26.dp)
                                    ) {
                                        Text("फोटो लें", fontSize = 10.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action Flow Buttons for rider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            when (order.status) {
                                OrderStatus.READY_FOR_PICKUP -> {
                                    Button(
                                        onClick = {
                                            onConfirmPackingPhoto(order.id)
                                            onAdvanceStatus(order.id, OrderStatus.PICKED_UP)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("पिकअप संपन्न करें (OTP: ${order.pickupOtp})", fontSize = 11.sp)
                                    }
                                }
                                OrderStatus.PICKED_UP -> {
                                    Button(
                                        onClick = { onAdvanceStatus(order.id, OrderStatus.OUT_FOR_DELIVERY) },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("डिलीवरी हेतु निकलें (Out for Delivery)", fontSize = 11.sp)
                                    }
                                }
                                OrderStatus.OUT_FOR_DELIVERY -> {
                                    Button(
                                        onClick = { onAdvanceStatus(order.id, OrderStatus.DELIVERED) },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("डिलीवर हुआ मार्क करें (OTP: ${order.deliveryOtp})", fontSize = 11.sp)
                                    }
                                }
                                OrderStatus.DELIVERED -> {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = ForestGreenLight,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "✓ सुरक्षित सुपुर्दगी संपन्न • ₹50 अर्जित",
                                            color = ForestGreen,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                                else -> {
                                    OutlinedButton(
                                        onClick = { onOpenTrip(order) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("मैप नेविगेशन खोलें", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
