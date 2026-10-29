package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.PotteryCategory
import com.example.data.model.PotteryProduct
import com.example.data.model.PotteryShop
import com.example.ui.components.PotterShopCard
import com.example.ui.components.PotteryCard
import com.example.ui.theme.*

@Composable
fun CustomerHomeScreen(
    location: String,
    searchQuery: String,
    isVoiceSearching: Boolean,
    selectedCategory: PotteryCategory,
    selectedDistanceKm: Int,
    products: List<PotteryProduct>,
    shops: List<PotteryShop>,
    wishlistIds: Set<String>,
    favoriteShopIds: Set<String>,
    language: AppLanguage,
    onSearchChange: (String) -> Unit,
    onVoiceSearch: () -> Unit,
    onSelectCategory: (PotteryCategory) -> Unit,
    onSelectDistance: (Int) -> Unit,
    onProductClick: (PotteryProduct) -> Unit,
    onAddToCart: (PotteryProduct) -> Unit,
    onToggleWishlist: (String) -> Unit,
    onShopClick: (PotteryShop) -> Unit,
    onToggleFavoriteShop: (String) -> Unit,
    onViewAllShops: () -> Unit,
    onOpenCustomOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
            .testTag("customer_home_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // 1. LOCATION BAR
        item {
            Surface(
                color = DarkChocolate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = AntiqueGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "डिलीवरी लोकेशन: $location" else "Deliver to: $location",
                        color = WarmCream,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Change Location",
                        tint = AntiqueGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // 2. SEARCH & VOICE SEARCH BAR
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkChocolate)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = {
                            Text(
                                text = if (language == AppLanguage.HINDI) "मिट्टी के मटके, कुल्हड़, हांडी खोजें..." else "Search earthen pots, kulhads...",
                                fontSize = 12.sp,
                                color = MutedText
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Terracotta
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = MutedText
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = AntiqueGold,
                            unfocusedBorderColor = CardBorderGold
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("search_text_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Voice Search Mic Button
                    IconButton(
                        onClick = onVoiceSearch,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isVoiceSearching) ErrorRed else Terracotta)
                            .border(1.dp, AntiqueGold, RoundedCornerShape(12.dp))
                            .testTag("voice_search_mic_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Search",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Voice search listening indicator
                AnimatedVisibility(visible = isVoiceSearching) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "🎙️ सुन रहे हैं... बोलिए (उदा. 'मटका')" else "🎙️ Listening... Speak now (e.g. 'Matka')",
                            color = AntiqueGoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 3. DISTANCE FILTER CHIPS (Requirement 3A.5: 2 KM, 5 KM, 10 KM, 20 KM)
        item {
            Column(modifier = Modifier.padding(top = 10.dp, start = 14.dp, end = 14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "दूरी फिल्टर (Distance Filter):" else "Radius Filter:",
                        color = DarkChocolate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (language == AppLanguage.HINDI) "नजदीकी कुम्हार पहले" else "Nearby Potters First",
                        color = TerracottaDark,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val distanceOptions = listOf(2, 5, 10, 20)
                    distanceOptions.forEach { km ->
                        val isSelected = selectedDistanceKm == km
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectDistance(km) },
                            label = {
                                Text(
                                    text = "$km KM",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Terracotta,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = DarkChocolate
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) AntiqueGold else CardBorderGold
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 4. HERO BANNER
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkChocolate),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AntiqueGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .testTag("hero_banner_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    // Background Image
                    Image(
                        painter = painterResource(id = R.drawable.img_pottery_hero),
                        contentDescription = "Indian Pottery Artisan Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Warm gradient shade overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        DarkChocolate.copy(alpha = 0.88f),
                                        DarkChocolate.copy(alpha = 0.45f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Text Content
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = AntiqueGold
                        ) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "पारंपरिक भारतीय हस्तकला" else "Traditional Indian Pottery",
                                color = DarkChocolate,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (language == AppLanguage.HINDI) "मिट्टी की कला,\nहर घर तक" else "Art of Earth,\nIn Every Home",
                            color = WarmCream,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 22.sp
                        )

                        Text(
                            text = if (language == AppLanguage.HINDI) "100% प्राकृतिक • रसायन मुक्त • स्वास्थ्यवर्धक" else "100% Pure Clay • Chemical-Free",
                            color = AntiqueGoldLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        // 5. POTTERY CATEGORIES HORIZONTAL CAROUSEL
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                PaddingValues(horizontal = 14.dp)
                Text(
                    text = if (language == AppLanguage.HINDI) "बर्तनों की श्रेणियां (Categories)" else "Explore Pottery Categories",
                    color = DarkChocolate,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(PotteryCategory.values()) { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            onClick = { onSelectCategory(category) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Terracotta else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                1.2.dp,
                                if (isSelected) AntiqueGold else CardBorderGold
                            ),
                            shadowElevation = if (isSelected) 3.dp else 1.dp,
                            modifier = Modifier.testTag("category_chip_${category.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = category.defaultIcon,
                                    contentDescription = category.nameEn,
                                    tint = if (isSelected) WarmCream else Terracotta,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.HINDI) category.nameHi else category.nameEn,
                                    color = if (isSelected) Color.White else DarkChocolate,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. CUSTOM POTTERY REQUEST BANNER
        item {
            Card(
                onClick = onOpenCustomOrder,
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmCreamSurface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AntiqueGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("custom_pottery_banner")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
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
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Custom",
                            tint = DarkChocolate,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "मनपसंद मिट्टी का बर्तन बनवाएं?" else "Need Custom Pottery Design?",
                            color = DarkChocolate,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (language == AppLanguage.HINDI) "स्थानीय कुम्हारों को अपना खास डिजाइन या आकार भेजें" else "Send custom size & design requests to local artisans",
                            color = MutedText,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = "Order Custom",
                        tint = Terracotta,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // 7. NEARBY POTTERS SECTION
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.HINDI) "आसपास के कुम्हार (Nearby Artisans)" else "Pottery Workshops Nearby",
                            color = DarkChocolate,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = if (language == AppLanguage.HINDI) "${selectedDistanceKm} KM के दायरे में उपलब्ध" else "Within ${selectedDistanceKm} KM radius",
                            color = MutedText,
                            fontSize = 11.sp
                        )
                    }

                    TextButton(onClick = onViewAllShops) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "सभी देखें" else "View All",
                            color = TerracottaDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(shops.take(3)) { shop ->
                        PotterShopCard(
                            shop = shop,
                            language = language,
                            isFavorite = favoriteShopIds.contains(shop.id),
                            onShopClick = { onShopClick(shop) },
                            onToggleFavorite = { onToggleFavoriteShop(shop.id) },
                            modifier = Modifier.width(280.dp)
                        )
                    }
                }
            }
        }

        // 8. HANDCRAFTED POTTERY GRID HEADER
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "प्राकृतिक मिट्टी के बर्तन (${products.size})" else "Handmade Pottery Products (${products.size})",
                        color = DarkChocolate,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = WarmCreamSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold)
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "प्राकृतिक रंग व फिनिश" else "Natural Clay",
                            color = TerracottaDark,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // 9. PRODUCTS LISTING (2 items per row)
        val productChunks = products.chunked(2)
        items(productChunks) { chunk ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                chunk.forEach { product ->
                    Box(modifier = Modifier.weight(1f)) {
                        PotteryCard(
                            product = product,
                            language = language,
                            isWishlisted = wishlistIds.contains(product.id),
                            onProductClick = { onProductClick(product) },
                            onAddToCart = { onAddToCart(product) },
                            onToggleWishlist = { onToggleWishlist(product.id) }
                        )
                    }
                }
                if (chunk.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
