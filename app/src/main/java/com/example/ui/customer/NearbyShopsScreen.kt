package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.PotteryShop
import com.example.ui.components.PotterShopCard
import com.example.ui.theme.*

@Composable
fun NearbyShopsScreen(
    shops: List<PotteryShop>,
    selectedDistanceKm: Int,
    favoriteShopIds: Set<String>,
    language: AppLanguage,
    onSelectDistance: (Int) -> Unit,
    onShopClick: (PotteryShop) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
            .testTag("nearby_shops_screen")
    ) {
        // Distance Filter Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = "Radius",
                        tint = Terracotta,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "नजदीकी कुम्हारों की दुकानें (${shops.size})" else "Nearby Pottery Workshops (${shops.size})",
                        color = DarkChocolate,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val distances = listOf(2, 5, 10, 20)
                    distances.forEach { km ->
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
                                containerColor = WarmCreamSurface,
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

        // Shops List
        if (shops.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (language == AppLanguage.HINDI) "इस दूरी में कोई कुम्हार दुकान नहीं मिली। कृपया 20 KM फिल्टर चुनें।"
                    else "No pottery workshops within this radius. Try selecting 20 KM.",
                    color = MutedText,
                    fontSize = 13.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(shops) { shop ->
                    PotterShopCard(
                        shop = shop,
                        language = language,
                        isFavorite = favoriteShopIds.contains(shop.id),
                        onShopClick = { onShopClick(shop) },
                        onToggleFavorite = { onToggleFavorite(shop.id) }
                    )
                }
            }
        }
    }
}
