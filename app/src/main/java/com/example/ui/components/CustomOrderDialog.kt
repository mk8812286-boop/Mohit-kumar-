package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppLanguage
import com.example.data.model.PotteryShop
import com.example.ui.theme.*

@Composable
fun CustomOrderDialog(
    shops: List<PotteryShop>,
    language: AppLanguage,
    onSubmitCustomOrder: (
        name: String,
        phone: String,
        shopName: String,
        description: String,
        dimensions: String,
        budget: Double
    ) -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    var customerName by remember { mutableStateOf("राजेश शर्मा") }
    var customerPhone by remember { mutableStateOf("+91 98290 12345") }
    var selectedShopIndex by remember { mutableStateOf(0) }
    var potteryDescription by remember { mutableStateOf("") }
    var dimensions by remember { mutableStateOf("ऊंचाई 30 cm, क्षमता 10 L") }
    var budgetText by remember { mutableStateOf("800") }

    val currentShop = shops.getOrNull(selectedShopIndex) ?: shops.first()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(2.dp, AntiqueGold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("custom_order_dialog")
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
                            .background(WarmCream),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Custom",
                            tint = Terracotta,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.HINDI) "कस्टम मिट्टी बर्तन अनुरोध" else "Custom Pottery Request",
                            color = DarkChocolate,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (language == AppLanguage.HINDI) "स्थानीय कुम्हार आपकी पसंद का बर्तन बनाएंगे" else "Local artisans will craft your custom design",
                            color = MutedText,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Select Potter Shop
                Text(
                    text = if (language == AppLanguage.HINDI) "कुम्हार चुनें:" else "Select Potter Shop:",
                    color = DarkChocolate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = WarmCreamSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = if (language == AppLanguage.HINDI) currentShop.nameHi else currentShop.nameEn,
                            color = DarkChocolate,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${currentShop.potterNameHi} • ${currentShop.distanceKm} KM",
                            color = TerracottaDark,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                Text(
                    text = if (language == AppLanguage.HINDI) "बर्तन का प्रकार व डिजाइन विवरण:" else "Pottery Design Details:",
                    color = DarkChocolate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = potteryDescription,
                    onValueChange = { potteryDescription = it },
                    placeholder = {
                        Text(
                            text = if (language == AppLanguage.HINDI) "उदा. नक्काशीदार पूजा कलश, बड़ा गमला या खास आकार की हांडी..." else "E.g. Large carved temple kalash...",
                            fontSize = 11.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Terracotta,
                        unfocusedBorderColor = CardBorderGold
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Dimensions
                Text(
                    text = if (language == AppLanguage.HINDI) "अनुमानित आकार / क्षमता:" else "Dimensions & Capacity:",
                    color = DarkChocolate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = dimensions,
                    onValueChange = { dimensions = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Terracotta,
                        unfocusedBorderColor = CardBorderGold
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Target Budget (₹)
                Text(
                    text = if (language == AppLanguage.HINDI) "आपका अनुमानित बजट (₹):" else "Your Estimated Budget (₹):",
                    color = DarkChocolate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = budgetText,
                    onValueChange = { budgetText = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Terracotta,
                        unfocusedBorderColor = CardBorderGold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
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
                            val desc = if (potteryDescription.isBlank()) {
                                if (language == AppLanguage.HINDI) "पारंपरिक विशेष डिजाइन का मटका" else "Special custom designed matka"
                            } else potteryDescription
                            val b = budgetText.toDoubleOrNull() ?: 800.0

                            onSubmitCustomOrder(
                                customerName,
                                customerPhone,
                                if (language == AppLanguage.HINDI) currentShop.nameHi else currentShop.nameEn,
                                desc,
                                dimensions,
                                b
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("submit_custom_order_btn")
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "भेजें (Submit)" else "Send to Potter",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
