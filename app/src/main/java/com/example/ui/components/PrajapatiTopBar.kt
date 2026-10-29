package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrajapatiTopBar(
    role: UserRole,
    language: AppLanguage,
    cartCount: Int,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    onRoleClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkChocolate)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Back button or App Logo/Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                if (showBackButton) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .testTag("topbar_back_button")
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AntiqueGold
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                } else {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Terracotta)
                            .border(1.5.dp, AntiqueGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = "Pottery",
                            tint = WarmCream,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Column {
                    Text(
                        text = if (language == AppLanguage.HINDI) "प्रजापति कला" else "Prajapati Kala",
                        color = WarmCream,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (language == AppLanguage.HINDI) "मिट्टी की कला, हर घर तक" else "Handcrafted Pottery Marketplace",
                        color = AntiqueGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }

            // Right Actions: Language Switcher, Role Badge, Cart (if Customer)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Language Toggle Pill
                Surface(
                    onClick = onLanguageClick,
                    shape = RoundedCornerShape(20.dp),
                    color = TerracottaDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold),
                    modifier = Modifier.testTag("language_toggle_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Language",
                            tint = AntiqueGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "English" else "हिंदी",
                            color = WarmCream,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Role Switcher Chip
                Surface(
                    onClick = onRoleClick,
                    shape = RoundedCornerShape(20.dp),
                    color = when (role) {
                        UserRole.CUSTOMER -> Terracotta
                        UserRole.SELLER -> Color(0xFF6D3C1F)
                        UserRole.DELIVERY -> ForestGreen
                        UserRole.ADMIN -> Color(0xFF7A2020)
                    },
                    border = androidx.compose.foundation.BorderStroke(1.dp, AntiqueGold),
                    modifier = Modifier.testTag("role_switcher_chip")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (role) {
                                UserRole.CUSTOMER -> Icons.Default.Person
                                UserRole.SELLER -> Icons.Default.Storefront
                                UserRole.DELIVERY -> Icons.Default.TwoWheeler
                                UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                            },
                            contentDescription = "Role",
                            tint = AntiqueGoldLight,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when (role) {
                                UserRole.CUSTOMER -> if (language == AppLanguage.HINDI) "ग्राहक" else "Customer"
                                UserRole.SELLER -> if (language == AppLanguage.HINDI) "कुम्हार" else "Seller"
                                UserRole.DELIVERY -> if (language == AppLanguage.HINDI) "डिलीवरी" else "Rider"
                                UserRole.ADMIN -> if (language == AppLanguage.HINDI) "एडमिन" else "Admin"
                            },
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Switch Role",
                            tint = AntiqueGoldLight,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Cart Icon (Customer Only)
                if (role == UserRole.CUSTOMER) {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(
                                    containerColor = AntiqueGold,
                                    contentColor = DarkChocolate
                                ) {
                                    Text(
                                        text = cartCount.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 2.dp)
                    ) {
                        IconButton(
                            onClick = onCartClick,
                            modifier = Modifier
                                .testTag("topbar_cart_button")
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Shopping Cart",
                                tint = WarmCream,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }

        // Gold decorative accent border line below top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.5.dp)
                .background(AntiqueGold)
        )
    }
}
