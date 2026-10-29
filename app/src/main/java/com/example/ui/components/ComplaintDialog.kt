package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.Order
import com.example.ui.theme.*

@Composable
fun ComplaintDialog(
    order: Order,
    language: AppLanguage,
    onSubmitComplaint: (
        orderNumber: String,
        productName: String,
        issueType: String,
        description: String,
        resolutionType: String,
        potterShopName: String,
        refundAmount: Double
    ) -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    val issueTypes = listOf(
        if (language == AppLanguage.HINDI) "रास्ते में टूटा / दरार (Cracked in Transit)" else "Broken or Cracked in Transit",
        if (language == AppLanguage.HINDI) "पानी का रिसाव / छिद्र खराबी (Leakage Issue)" else "Clay Porosity / Severe Leakage",
        if (language == AppLanguage.HINDI) "गलत बर्तन / गलत साइज (Wrong Item/Size)" else "Wrong Item or Size Received",
        if (language == AppLanguage.HINDI) "खराब फिनिश / बिना पका हुआ (Defective Finish)" else "Defective Finish or Clay Quality"
    )

    var selectedIssue by remember { mutableStateOf(issueTypes[0]) }
    var description by remember { mutableStateOf("") }
    var selectedResolution by remember {
        mutableStateOf(if (language == AppLanguage.HINDI) "नया बर्तन बदलें (Replacement)" else "Replacement with New Pottery")
    }
    var photoAttached by remember { mutableStateOf(true) }

    val productName = order.items.firstOrNull()?.product?.nameHi ?: "मिट्टी का बर्तन"
    val shopName = order.items.firstOrNull()?.product?.shopNameHi ?: "कुम्हार दुकान"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(2.dp, CardBorderGold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("complaint_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(scrollState)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ErrorRedBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = "Complaint",
                            tint = ErrorRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.HINDI) "खराब बर्तन शिकायत दर्ज करें" else "Report Defective Pottery",
                            color = DarkChocolate,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (language == AppLanguage.HINDI) "ऑर्डर #${order.orderNumber} • ₹${order.totalAmount.toInt()}" else "Order #${order.orderNumber}",
                            color = MutedText,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Issue Type Selection
                Text(
                    text = if (language == AppLanguage.HINDI) "1. समस्या का प्रकार चुनें:" else "1. Select Problem Type:",
                    color = DarkChocolate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                issueTypes.forEach { issue ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedIssue = issue }
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedIssue == issue),
                            onClick = { selectedIssue = issue },
                            colors = RadioButtonDefaults.colors(selectedColor = Terracotta)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = issue,
                            color = CharcoalText,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2. Photo Upload Simulation
                Text(
                    text = if (language == AppLanguage.HINDI) "2. खराब बर्तन की फोटो अपलोड करें:" else "2. Upload Photo of Defect:",
                    color = DarkChocolate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = WarmCreamSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { photoAttached = !photoAttached }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pottery_logo),
                            contentDescription = "Defect photo",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Terracotta, RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (photoAttached)
                                    (if (language == AppLanguage.HINDI) "फोटो संलग्न है: IMG_POTTERY_CRACK.JPG" else "Photo attached: crack_evidence.jpg")
                                else
                                    (if (language == AppLanguage.HINDI) "कैमरा खोलें / फोटो जोड़ें" else "Tap to take photo"),
                                color = DarkChocolate,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) "कुम्हार व एडमिन दोनों द्वारा जांची जाएगी" else "Will be inspected by artisan & admin",
                                color = MutedText,
                                fontSize = 10.sp
                            )
                        }
                        Icon(
                            imageVector = if (photoAttached) Icons.Default.CheckCircle else Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = if (photoAttached) ForestGreen else Terracotta,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3. Description
                Text(
                    text = if (language == AppLanguage.HINDI) "3. समस्या का विवरण लिखें:" else "3. Describe Issue:",
                    color = DarkChocolate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = {
                        Text(
                            text = if (language == AppLanguage.HINDI) "जैसे: हांडी की तली से पानी रिस रहा है या मटका टूटा मिला..." else "Describe defect in detail...",
                            fontSize = 11.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .testTag("complaint_description_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Terracotta,
                        unfocusedBorderColor = CardBorderGold
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 4. Resolution Type
                Text(
                    text = if (language == AppLanguage.HINDI) "4. आप क्या चाहते हैं?" else "4. Desired Resolution:",
                    color = DarkChocolate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                val resolutions = listOf(
                    if (language == AppLanguage.HINDI) "नया बर्तन बदलें (Replacement)" else "Replacement with New Pottery",
                    if (language == AppLanguage.HINDI) "पूरा रिफंड बैंक/UPI में (Full Refund)" else "Full Refund to UPI / Bank"
                )

                resolutions.forEach { res ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedResolution = res }
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedResolution == res),
                            onClick = { selectedResolution = res },
                            colors = RadioButtonDefaults.colors(selectedColor = Terracotta)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = res,
                            color = CharcoalText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "रद्द करें" else "Cancel",
                            color = MutedText
                        )
                    }

                    Button(
                        onClick = {
                            val finalDesc = if (description.isBlank()) {
                                if (language == AppLanguage.HINDI) "पार्सल खोलते ही बर्तन टूटा हुआ मिला।" else "Found damaged item on delivery."
                            } else description

                            onSubmitComplaint(
                                order.orderNumber,
                                productName,
                                selectedIssue,
                                finalDesc,
                                selectedResolution,
                                shopName,
                                order.totalAmount
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("submit_complaint_confirm_btn")
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "दर्ज करें" else "Submit",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
