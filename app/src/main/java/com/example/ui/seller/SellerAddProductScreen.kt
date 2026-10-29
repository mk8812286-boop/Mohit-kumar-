package com.example.ui.seller

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
import com.example.R
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun SellerAddProductScreen(
    language: AppLanguage,
    onSaveProduct: (PotteryProduct) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var nameHi by remember { mutableStateOf("पारंपरिक लाल मिट्टी का मटका") }
    var nameEn by remember { mutableStateOf("Traditional Red Clay Matka") }
    var selectedCategory by remember { mutableStateOf(PotteryCategory.MATKA) }
    var priceText by remember { mutableStateOf("320") }
    var originalPriceText by remember { mutableStateOf("450") }
    var capacityText by remember { mutableStateOf("5.0 L") }
    var dimensionsText by remember { mutableStateOf("22 x 22 cm") }
    var weightText by remember { mutableStateOf("2.0 kg") }
    var finishType by remember { mutableStateOf("छिद्रयुक्त बिना पॉलिश प्राकृतिक मिट्टी") }
    var stockQuantityText by remember { mutableStateOf("15") }
    var careGuide by remember { mutableStateOf("पहले उपयोग से पूर्व 12 घंटे साफ पानी में भिगोकर रखें।") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
            .testTag("seller_add_product_screen")
    ) {
        // Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = DarkChocolate
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "नया मिट्टी का बर्तन जोड़ें" else "Add Handcrafted Pottery",
                    color = DarkChocolate,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Photo Upload Preview
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = WarmCreamSurface,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AntiqueGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pottery_logo),
                        contentDescription = "New pottery photo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Terracotta, RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.HINDI) "बर्तन की प्राकृतिक फोटो" else "Pottery Photo Attached",
                            color = DarkChocolate,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (language == AppLanguage.HINDI) "प्राकृतिक रोशनी में ली गई असली फोटो" else "Real natural clay photo",
                            color = MutedText,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedButton(
                            onClick = { /* camera simulation */ },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("फोटो बदलें", fontSize = 10.sp)
                        }
                    }
                }
            }

            // Product Name (Hindi)
            OutlinedTextField(
                value = nameHi,
                onValueChange = { nameHi = it },
                label = { Text("बर्तन का नाम (हिंदी)", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            // Product Name (English)
            OutlinedTextField(
                value = nameEn,
                onValueChange = { nameEn = it },
                label = { Text("Product Name (English)", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            // Price & Original Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("बिक्री मूल्य (₹)", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = originalPriceText,
                    onValueChange = { originalPriceText = it },
                    label = { Text("एमआरपी (₹)", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            // Capacity & Weight
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = capacityText,
                    onValueChange = { capacityText = it },
                    label = { Text("क्षमता (उदा. 5.0 L)", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("वजन (उदा. 2 kg)", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            // Dimensions & Stock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = dimensionsText,
                    onValueChange = { dimensionsText = it },
                    label = { Text("माप (उदा. 24 x 24 cm)", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = stockQuantityText,
                    onValueChange = { stockQuantityText = it },
                    label = { Text("स्टॉक संख्या", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            // Clay Finish
            OutlinedTextField(
                value = finishType,
                onValueChange = { finishType = it },
                label = { Text("फिनिश व मिट्टी का प्रकार", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            // Care Guide
            OutlinedTextField(
                value = careGuide,
                onValueChange = { careGuide = it },
                label = { Text("रखरखाव व उपयोग के निर्देश", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }

        // Save Button Bar
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Button(
                onClick = {
                    val p = priceText.toDoubleOrNull() ?: 300.0
                    val op = originalPriceText.toDoubleOrNull() ?: 400.0
                    val stock = stockQuantityText.toIntOrNull() ?: 10

                    val newProduct = PotteryProduct(
                        id = "prod_${System.currentTimeMillis()}",
                        nameHi = nameHi,
                        nameEn = nameEn,
                        descriptionHi = "$nameHi - बिना केमिकल शुद्ध प्राकृतिक मिट्टी से हस्तनिर्मित।",
                        descriptionEn = "$nameEn - Handcrafted pure natural clay pottery.",
                        category = selectedCategory,
                        basePrice = p,
                        originalPrice = op,
                        shopId = "shop_1",
                        shopNameHi = "रामेश्वर प्रजापति पॉटरी वर्क्स",
                        shopNameEn = "Rameshwar Prajapati Pottery Works",
                        potterNameHi = "रामेश्वर प्रजापति",
                        potterNameEn = "Rameshwar Prajapati",
                        rating = 5.0,
                        reviewCount = 1,
                        sizes = listOf(
                            ProductVariantSize("स्टैंडर्ड", "Standard", capacityText, dimensionsText, weightText, 0.0)
                        ),
                        colors = listOf(
                            ClayColor("प्राकृतिक लाल गेरू", "Natural Terracotta", 0xFFA65E3B)
                        ),
                        finishType = finishType,
                        careInstructionsHi = careGuide,
                        careInstructionsEn = careGuide,
                        stockQuantity = stock,
                        imageDrawableRes = R.drawable.ic_pottery_logo
                    )
                    onSaveProduct(newProduct)
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .height(48.dp)
                    .testTag("save_new_product_btn")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "बर्तन लिस्ट करें (Publish Product)" else "Publish Pottery Product",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
