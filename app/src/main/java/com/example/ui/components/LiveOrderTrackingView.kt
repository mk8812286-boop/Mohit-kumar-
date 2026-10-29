package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
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
fun LiveOrderTrackingView(
    order: Order,
    language: AppLanguage,
    onAdvanceStatus: (OrderStatus) -> Unit,
    onReportProblem: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Pulsing animation for active delivery indicator
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("live_order_tracking_view")
    ) {
        // Order Header Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, CardBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.HINDI) "ऑर्डर नंबर: ${order.orderNumber}" else "Order ID: ${order.orderNumber}",
                        color = DarkChocolate,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = if (language == AppLanguage.HINDI) "कुल राशि: ₹${order.totalAmount.toInt()} (${order.paymentMethod.titleHi})"
                        else "Total: ₹${order.totalAmount.toInt()} (${order.paymentMethod.titleEn})",
                        color = TerracottaDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Status Chip
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (order.status == OrderStatus.DELIVERED) ForestGreen else Terracotta,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) order.status.titleHi else order.status.titleEn,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SIMULATED GPS LIVE MAP VIEW
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, AntiqueGold),
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .testTag("gps_live_map_card")
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Canvas GPS Map representation with roads and delivery path
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Background grid representing streets
                    val streetColor = Color(0xFFE2D6C5)
                    drawLine(streetColor, Offset(0f, h * 0.3f), Offset(w, h * 0.3f), strokeWidth = 14f)
                    drawLine(streetColor, Offset(0f, h * 0.7f), Offset(w, h * 0.7f), strokeWidth = 12f)
                    drawLine(streetColor, Offset(w * 0.25f, 0f), Offset(w * 0.25f, h), strokeWidth = 10f)
                    drawLine(streetColor, Offset(w * 0.7f, 0f), Offset(w * 0.7f, h), strokeWidth = 12f)

                    // Delivery Route path (Terracotta dashed line)
                    val routePathColor = Color(0xFFA65E3B)
                    val p1 = Offset(w * 0.18f, h * 0.65f) // Potter Shop
                    val p2 = Offset(w * 0.45f, h * 0.65f)
                    val p3 = Offset(w * 0.55f, h * 0.35f)
                    val p4 = Offset(w * 0.82f, h * 0.35f) // Customer House

                    drawLine(
                        color = routePathColor,
                        start = p1,
                        end = p2,
                        strokeWidth = 6f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f))
                    )
                    drawLine(
                        color = routePathColor,
                        start = p2,
                        end = p3,
                        strokeWidth = 6f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f))
                    )
                    drawLine(
                        color = routePathColor,
                        start = p3,
                        end = p4,
                        strokeWidth = 6f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f))
                    )
                }

                // Marker 1: Kumhar Potter Workshop Pin
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 20.dp, top = 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(DarkChocolate)
                            .border(2.dp, AntiqueGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "Potter Shop",
                            tint = WarmCream,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = if (language == AppLanguage.HINDI) "कुम्हार की दुकान" else "Potter Shop",
                        color = DarkChocolate,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                // Marker 2: Customer Destination Pin
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 20.dp, bottom = 45.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(ForestGreen)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Customer Home",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = if (language == AppLanguage.HINDI) "आपका घर" else "Your Home",
                        color = ForestGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                // Marker 3: Live Rider / Delivery Partner (Moving on the map)
                val riderAlignment = when (order.status) {
                    OrderStatus.PLACED, OrderStatus.SELLER_ACCEPTED, OrderStatus.PACKING -> Alignment.CenterStart
                    OrderStatus.READY_FOR_PICKUP, OrderStatus.PARTNER_ASSIGNED, OrderStatus.PICKED_UP -> Alignment.Center
                    OrderStatus.OUT_FOR_DELIVERY -> Alignment.Center
                    OrderStatus.DELIVERED -> Alignment.CenterEnd
                    OrderStatus.CANCELLED -> Alignment.Center
                }

                Box(
                    modifier = Modifier
                        .align(riderAlignment)
                        .padding(horizontal = 70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Terracotta.copy(alpha = pulseAlpha))
                            .border(2.dp, AntiqueGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TwoWheeler,
                            contentDescription = "Rider",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Live status banner overlay on top of map
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp),
                    color = DarkChocolate.copy(alpha = 0.92f),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF00E676))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "लाइव GPS ट्रैकिंग सक्रिय" else "Live GPS Route Active",
                                color = WarmCream,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (order.status != OrderStatus.DELIVERED) {
                            Text(
                                text = if (language == AppLanguage.HINDI)
                                    "पहुंचने का समय: ~${order.liveEstimatedMinutes} मिनट (${order.liveDistanceRemainingKm} KM)"
                                else
                                    "ETA: ~${order.liveEstimatedMinutes} mins (${order.liveDistanceRemainingKm} KM)",
                                color = AntiqueGoldLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                text = if (language == AppLanguage.HINDI) "डिलीवरी संपन्न" else "Delivered Safely",
                                color = Color(0xFF00E676),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // OTP SECURITY CARD (Requirement 3B & 3C: Pickup OTP & Delivery OTP)
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WarmCream),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, AntiqueGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Delivery Confirmation OTP (Customer tells Rider)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "डिलीवरी पुष्टि OTP" else "Delivery OTP",
                        color = DarkChocolate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = order.deliveryOtp,
                        color = TerracottaDark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = if (language == AppLanguage.HINDI) "(राइडर को दें)" else "(Give to rider)",
                        color = MutedText,
                        fontSize = 9.sp
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp),
                    color = AntiqueGold.copy(alpha = 0.5f)
                )

                // Pickup OTP (Potter tells Rider)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "दुकान पिकअप OTP" else "Shop Pickup OTP",
                        color = DarkChocolate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = order.pickupOtp,
                        color = AntiqueGold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = if (language == AppLanguage.HINDI) "(कुम्हार द्वारा सत्यापित)" else "(Artisan verified)",
                        color = MutedText,
                        fontSize = 9.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Delivery Partner Contact Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Terracotta.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TwoWheeler,
                        contentDescription = "Driver",
                        tint = Terracotta,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.deliveryBoyName,
                        color = DarkChocolate,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (language == AppLanguage.HINDI) "सत्यापित डिलीवरी साथी • ${order.deliveryBoyPhone}"
                        else "Verified Partner • ${order.deliveryBoyPhone}",
                        color = MutedText,
                        fontSize = 11.sp
                    )
                }

                IconButton(
                    onClick = { /* simulated call */ },
                    modifier = Modifier
                        .size(36.dp)
                        .background(ForestGreen, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call Rider",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 8-STAGE DELIVERY TIMELINE (Requirement 7)
        Text(
            text = if (language == AppLanguage.HINDI) "ऑर्डर प्रगति स्थिति (Tracking Steps)" else "Order Progress Milestones",
            color = DarkChocolate,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(10.dp))

        val stages = listOf(
            OrderStatus.PLACED,
            OrderStatus.SELLER_ACCEPTED,
            OrderStatus.PACKING,
            OrderStatus.READY_FOR_PICKUP,
            OrderStatus.PARTNER_ASSIGNED,
            OrderStatus.PICKED_UP,
            OrderStatus.OUT_FOR_DELIVERY,
            OrderStatus.DELIVERED
        )

        val currentStep = order.status.stepIndex

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                stages.forEachIndexed { index, status ->
                    val isCompleted = currentStep >= status.stepIndex
                    val isCurrent = currentStep == status.stepIndex

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Step indicator circle
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCompleted -> ForestGreen
                                        isCurrent -> Terracotta
                                        else -> SoftBeige
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            } else {
                                Text(
                                    text = "${index + 1}",
                                    color = if (isCurrent) Color.White else MutedText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.HINDI) status.titleHi else status.titleEn,
                                color = if (isCompleted || isCurrent) DarkChocolate else MutedText,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }

                        if (isCurrent) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Terracotta.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) "सक्रिय" else "In Progress",
                                    color = TerracottaDark,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    if (index < stages.lastIndex) {
                        Box(
                            modifier = Modifier
                                .padding(start = 11.dp)
                                .width(2.dp)
                                .height(16.dp)
                                .background(if (currentStep > status.stepIndex) ForestGreen else SoftBeige)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Test Simulation Stepper Controls
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WarmCream),
            border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = if (language == AppLanguage.HINDI) "सिम्युलेशन: स्थिति आगे बढ़ाएं (Test Simulation)" else "Simulation: Advance Order Step",
                    color = DarkChocolate,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val nextIndex = (currentStep + 1).coerceAtMost(7)
                            onAdvanceStatus(stages[nextIndex])
                        },
                        enabled = currentStep < 7,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("advance_order_status_btn")
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "अगला चरण (Next Step)" else "Advance Step",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = onReportProblem,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("report_complaint_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = "Report",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "खराब बर्तन शिकायत" else "Report Defect",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
