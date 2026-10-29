package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppLanguage
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun RoleSwitcherDialog(
    currentRole: UserRole,
    language: AppLanguage,
    onRoleSelected: (UserRole) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = WarmCream),
            border = androidx.compose.foundation.BorderStroke(2.dp, AntiqueGold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("role_switcher_dialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkChocolate)
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "ऐप पैनल चुनें (Select App Panel)" else "Select Marketplace Panel",
                        color = AntiqueGold,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (language == AppLanguage.HINDI)
                        "प्रजापति कला 4 मुख्य पैनलों से संचालित होती है। आप किसी भी पैनल में प्रवेश कर सकते हैं:"
                    else
                        "Prajapati Kala powers 4 distinct panels. Switch freely to explore any role:",
                    color = CharcoalText,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                val roles = listOf(
                    Triple(
                        UserRole.CUSTOMER,
                        Icons.Default.Person,
                        if (language == AppLanguage.HINDI) "1. ग्राहक पैनल (Customer App)" to "नजदीकी कुम्हार खोजें, बर्तन खरीदें, ऑर्डर ट्रैक करें"
                        else "1. Customer App" to "Discover potters, buy pure earthenware, live GPS track"
                    ),
                    Triple(
                        UserRole.SELLER,
                        Icons.Default.Storefront,
                        if (language == AppLanguage.HINDI) "2. कुम्हार विक्रेता पैनल (Kumhar Seller)" to "दुकान रजिस्ट्रेशन, बर्तन जोड़ें, पैकिंग व कमाई"
                        else "2. Kumhar Seller App" to "Register shop, list pottery, manage packing & payouts"
                    ),
                    Triple(
                        UserRole.DELIVERY,
                        Icons.Default.TwoWheeler,
                        if (language == AppLanguage.HINDI) "3. डिलीवरी बॉय पैनल (Delivery Partner)" to "कुम्हार से पिकअप, पैकिंग फोटो, ग्राहक तक GPS नेविगेशन"
                        else "3. Delivery Partner App" to "Pick up from potters, verify package, route navigation"
                    ),
                    Triple(
                        UserRole.ADMIN,
                        Icons.Default.AdminPanelSettings,
                        if (language == AppLanguage.HINDI) "4. सुपर एडमिन पैनल (Super Admin)" to "दुकान सत्यापन, 10% कमीशन, शिकायतें व रिफंड प्रबंधन"
                        else "4. Super Admin Panel" to "Verify artisans, commission setup, complaints & refunds"
                    )
                )

                roles.forEach { (role, icon, textPair) ->
                    val isSelected = currentRole == role
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .testTag("role_option_${role.name.lowercase()}")
                            .clickable { onRoleSelected(role) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Terracotta else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (isSelected) AntiqueGold else CardBorderGold
                        ),
                        shadowElevation = if (isSelected) 3.dp else 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) WarmCream else SoftBeige),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) DarkChocolate else Terracotta,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = textPair.first,
                                    color = if (isSelected) Color.White else DarkChocolate,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = textPair.second,
                                    color = if (isSelected) WarmCream else MutedText,
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Active",
                                    tint = AntiqueGoldLight,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_role_dialog")
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "बंद करें (Close)" else "Close",
                        color = TerracottaDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
