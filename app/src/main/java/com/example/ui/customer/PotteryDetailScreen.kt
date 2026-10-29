package com.example.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.ClayColor
import com.example.data.model.PotteryProduct
import com.example.data.model.ProductVariantSize
import com.example.ui.theme.*

@Composable
fun PotteryDetailScreen(
    product: PotteryProduct,
    language: AppLanguage,
    isWishlisted: Boolean,
    onAddToCart: (ProductVariantSize, ClayColor, Int) -> Unit,
    onBuyNow: (ProductVariantSize, ClayColor, Int) -> Unit,
    onToggleWishlist: () -> Unit,
    onOpenCustomRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var selectedSize by remember { mutableStateOf(product.sizes.firstOrNull() ?: ProductVariantSize("सामान्य", "Regular", "5L", "20cm", "1.5kg")) }
    var selectedColor by remember { mutableStateOf(product.colors.firstOrNull() ?: ClayColor("लाल गेरू", "Terracotta Red", 0xFFA65E3B)) }
    var quantity by remember { mutableIntStateOf(1) }
    var selectedPhotoAngle by remember { mutableStateOf(0) } // 0: Front, 1: Close-up, 2: Dimensions, 3: Artisan

    val currentPrice = product.basePrice + selectedSize.priceDelta
    val currentOriginal = product.originalPrice + selectedSize.priceDelta

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
            .testTag("pottery_detail_screen")
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            // HERO IMAGE GALLERY
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(SoftBeige)
            ) {
                Image(
                    painter = painterResource(id = product.imageDrawableRes ?: R.drawable.ic_pottery_logo),
                    contentDescription = product.nameEn,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // 100% Pure Clay Tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkChocolate.copy(alpha = 0.9f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold),
                    modifier = Modifier
                        .padding(14.dp)
                        .align(Alignment.TopStart)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Certified",
                            tint = AntiqueGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "100% शुद्ध प्राकृतिक मिट्टी" else "100% Pure Natural Clay",
                            color = WarmCream,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Wishlist Button
                IconButton(
                    onClick = onToggleWishlist,
                    modifier = Modifier
                        .padding(14.dp)
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) ErrorRed else DarkChocolate,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Angle/Photography Tabs (Requirement 4: Natural Photography angles)
                Surface(
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val angles = listOf(
                            if (language == AppLanguage.HINDI) "सामने (Front)" else "Front View",
                            if (language == AppLanguage.HINDI) "क्लोजअप (Texture)" else "Clay Texture",
                            if (language == AppLanguage.HINDI) "माप (Scale)" else "Dimensions",
                            if (language == AppLanguage.HINDI) "कुम्हार चाक" else "Artisan Wheel"
                        )
                        angles.forEachIndexed { idx, label ->
                            val isSel = selectedPhotoAngle == idx
                            Text(
                                text = label,
                                color = if (isSel) AntiqueGold else Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier
                                    .clickable { selectedPhotoAngle = idx }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // DETAILS SECTION
            Column(modifier = Modifier.padding(16.dp)) {
                // Category & Stock Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Terracotta.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) product.category.nameHi else product.category.nameEn,
                            color = TerracottaDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(ForestGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "स्टॉक में उपलब्ध (${product.stockQuantity} नग)" else "In Stock (${product.stockQuantity} units)",
                            color = ForestGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Product Title
                Text(
                    text = if (language == AppLanguage.HINDI) product.nameHi else product.nameEn,
                    color = DarkChocolate,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Potter Shop info card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(WarmCream)
                                .border(1.dp, AntiqueGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = "Shop",
                                tint = Terracotta,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.HINDI) product.shopNameHi else product.shopNameEn,
                                color = DarkChocolate,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) "कारीगर: ${product.potterNameHi}" else "Artisan: ${product.potterNameEn}",
                                color = MutedText,
                                fontSize = 11.sp
                            )
                        }

                        // Rating pill
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ForestGreen
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${product.rating}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // PRICE BLOCK
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "₹${currentPrice.toInt()}",
                        color = TerracottaDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "₹${currentOriginal.toInt()}",
                        color = MutedText,
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val discountPercent = (((currentOriginal - currentPrice) / currentOriginal) * 100).toInt()
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = ForestGreenLight
                    ) {
                        Text(
                            text = "$discountPercent% छूट (OFF)",
                            color = ForestGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = if (language == AppLanguage.HINDI) "सभी टैक्स सहित • होम डिलीवरी उपलब्ध (₹${product.deliveryCharge.toInt()})"
                    else "Inclusive of all taxes • Delivery ₹${product.deliveryCharge.toInt()}",
                    color = MutedText,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 1. CHOOSE SIZE & CAPACITY (Requirement 4)
                Text(
                    text = if (language == AppLanguage.HINDI) "1. आकार और क्षमता चुनें (Size & Capacity):" else "1. Choose Size & Capacity:",
                    color = DarkChocolate,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    product.sizes.forEach { size ->
                        val isSelected = selectedSize.sizeNameEn == size.sizeNameEn
                        Surface(
                            onClick = { selectedSize = size },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) WarmCreamSurface else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isSelected) Terracotta else CardBorderGold
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedSize = size },
                                        colors = RadioButtonDefaults.colors(selectedColor = Terracotta)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.HINDI) size.sizeNameHi else size.sizeNameEn,
                                            color = DarkChocolate,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "क्षमता: ${size.capacity} • माप: ${size.dimensions} • वजन: ${size.weight}",
                                            color = MutedText,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                if (size.priceDelta > 0) {
                                    Text(
                                        text = "+₹${size.priceDelta.toInt()}",
                                        color = TerracottaDark,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. CHOOSE NATURAL CLAY COLOR (Requirement 4)
                Text(
                    text = if (language == AppLanguage.HINDI) "2. प्राकृतिक मिट्टी का रंग चुनें (Clay Color):" else "2. Choose Natural Clay Color:",
                    color = DarkChocolate,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    product.colors.forEach { colorItem ->
                        val isColorSelected = selectedColor.nameEn == colorItem.nameEn
                        Surface(
                            onClick = { selectedColor = colorItem },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isColorSelected) WarmCreamSurface else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isColorSelected) Terracotta else CardBorderGold
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(colorItem.colorHex))
                                        .border(1.5.dp, if (isColorSelected) AntiqueGold else Color.Gray, CircleShape)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (language == AppLanguage.HINDI) colorItem.nameHi.split(" ")[0] else colorItem.nameEn.split(" ")[0],
                                    color = DarkChocolate,
                                    fontSize = 10.sp,
                                    fontWeight = if (isColorSelected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. FINISH & CRAFTSMANSHIP
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Finish",
                            tint = Terracotta,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.HINDI) "बनावट व फिनिश (Texture & Finish):" else "Texture & Craftsmanship:",
                                color = DarkChocolate,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = product.finishType,
                                color = MutedText,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. CARE & USAGE INSTRUCTIONS (Requirement 4.9: उपयोग और देखभाल की जानकारी)
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, AntiqueGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TipsAndUpdates,
                                contentDescription = "Care Instructions",
                                tint = AntiqueGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "मिट्टी बर्तन उपयोग व देखभाल नियम (Care Guide)" else "Clay Pottery Care & First Use",
                                color = DarkChocolate,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == AppLanguage.HINDI) product.careInstructionsHi else product.careInstructionsEn,
                            color = CharcoalText,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 5. DESCRIPTION
                Text(
                    text = if (language == AppLanguage.HINDI) "विस्तृत विवरण (Description):" else "Product Details:",
                    color = DarkChocolate,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (language == AppLanguage.HINDI) product.descriptionHi else product.descriptionEn,
                    color = CharcoalText,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Custom order prompt
                Surface(
                    onClick = onOpenCustomRequest,
                    shape = RoundedCornerShape(8.dp),
                    color = WarmCream,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Handyman,
                            contentDescription = null,
                            tint = Terracotta,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "क्या आप इस बर्तन में अलग डिजाइन चाहते हैं? कस्टम अनुरोध भेजें"
                            else "Need custom changes to this pottery? Request Custom Order",
                            color = DarkChocolate,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // STICKY BOTTOM BAR (Quantity Stepper, Add to Cart, Buy Now)
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Quantity Stepper
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(SoftBeige, RoundedCornerShape(8.dp))
                        .border(1.dp, CardBorderGold, RoundedCornerShape(8.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "$quantity",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    IconButton(
                        onClick = { if (quantity < product.stockQuantity) quantity++ },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Add to Cart Button
                OutlinedButton(
                    onClick = { onAddToCart(selectedSize, selectedColor, quantity) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Terracotta),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Terracotta),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("detail_add_to_cart_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "कार्ट में जोड़ें" else "Add to Cart",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Buy Now Button
                Button(
                    onClick = { onBuyNow(selectedSize, selectedColor, quantity) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("detail_buy_now_btn")
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "अभी खरीदें" else "Buy Now",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
