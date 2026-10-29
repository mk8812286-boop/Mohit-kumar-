package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.PotteryProduct
import com.example.ui.theme.*

@Composable
fun PotteryCard(
    product: PotteryProduct,
    language: AppLanguage,
    isWishlisted: Boolean,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit,
    onToggleWishlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("pottery_card_${product.id}")
            .fillMaxWidth()
            .clickable { onProductClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, CardBorderGold),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image Box with badges & wishlist button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
                    .background(SoftBeige)
            ) {
                // Real product photo
                Image(
                    painter = painterResource(id = product.imageDrawableRes ?: R.drawable.ic_pottery_logo),
                    contentDescription = product.nameEn,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Pure Clay badge (top left)
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(12.dp),
                    color = DarkChocolate.copy(alpha = 0.88f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Pure Clay",
                            tint = AntiqueGold,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "100% शुद्ध मिट्टी" else "Pure Clay",
                            color = WarmCream,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Wishlist heart button (top right)
                IconButton(
                    onClick = onToggleWishlist,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(32.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(50))
                        .testTag("wishlist_btn_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) ErrorRed else DarkChocolate,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Clay finish tag (bottom left)
                Surface(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.BottomStart),
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Black.copy(alpha = 0.55f)
                ) {
                    Text(
                        text = if (product.sizes.isNotEmpty()) product.sizes[0].capacity else "Handcrafted",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Info Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Title
                Text(
                    text = if (language == AppLanguage.HINDI) product.nameHi else product.nameEn,
                    color = DarkChocolate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Potter artisan name
                Text(
                    text = if (language == AppLanguage.HINDI) "शिल्पी: ${product.potterNameHi}" else "Artisan: ${product.potterNameEn}",
                    color = MutedText,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )

                // Rating & Reviews
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = ForestGreen,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${product.rating}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(9.dp)
                            )
                        }
                    }
                    Text(
                        text = "(${product.reviewCount})",
                        color = MutedText,
                        fontSize = 10.sp
                    )
                }

                // Price and Add Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹${product.basePrice.toInt()}",
                                color = TerracottaDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Serif
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "₹${product.originalPrice.toInt()}",
                                color = MutedText,
                                fontSize = 11.sp,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                        val discount = (((product.originalPrice - product.basePrice) / product.originalPrice) * 100).toInt()
                        Text(
                            text = "$discount% छूट (OFF)",
                            color = ForestGreen,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Quick Add button
                    Button(
                        onClick = onAddToCart,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Terracotta,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("add_to_cart_btn_${product.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "जोड़ें" else "Add",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
