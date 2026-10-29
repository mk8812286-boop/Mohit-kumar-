package com.example.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.OrderItem
import com.example.data.model.PaymentMethod
import com.example.ui.theme.*

@Composable
fun CartScreen(
    cartItems: List<OrderItem>,
    customerLocation: String,
    language: AppLanguage,
    onUpdateQuantity: (index: Int, qty: Int) -> Unit,
    onPlaceOrder: (name: String, phone: String, address: String, payment: PaymentMethod) -> Unit,
    onBrowseProducts: () -> Unit,
    modifier: Modifier = Modifier
) {
    var customerName by remember { mutableStateOf("राजेश शर्मा (Rajesh Sharma)") }
    var customerPhone by remember { mutableStateOf("+91 98290 12345") }
    var deliveryAddress by remember { mutableStateOf(customerLocation) }
    var selectedPayment by remember { mutableStateOf(PaymentMethod.UPI) }

    val subtotal = cartItems.sumOf { it.unitPrice * it.quantity }
    val deliveryFee = if (subtotal > 500.0 || cartItems.isEmpty()) 0.0 else 40.0
    val total = subtotal + deliveryFee

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(WarmCream)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(SoftBeige),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveShoppingCart,
                        contentDescription = "Empty Cart",
                        tint = Terracotta,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (language == AppLanguage.HINDI) "आपकी कार्ट अभी खाली है" else "Your Cart is Empty",
                    color = DarkChocolate,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )

                Text(
                    text = if (language == AppLanguage.HINDI) "अपने घर के लिए प्राकृतिक मिट्टी के मटके, कुल्हड़ व हांडी चुनें"
                    else "Explore handcrafted earthen pots & kitchenware",
                    color = MutedText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                )

                Button(
                    onClick = onBrowseProducts,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                    modifier = Modifier.testTag("browse_pottery_btn")
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "मिट्टी के बर्तन देखें (Browse Pottery)" else "Browse Pure Pottery",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(WarmCream)
                .testTag("cart_screen")
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Free Delivery Banner
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ForestGreenLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (subtotal >= 500)
                                    (if (language == AppLanguage.HINDI) "बधाई! आपको निःशुल्क सुरक्षित मिट्टी डिलीवरी मिल रही है।" else "Eligible for FREE Safe Pottery Delivery!")
                                else
                                    (if (language == AppLanguage.HINDI) "₹${(500 - subtotal).toInt()} और जोड़ें और पाएं मुफ़्त होम डिलीवरी" else "Add ₹${(500 - subtotal).toInt()} more for FREE Delivery"),
                                color = ForestGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Cart Items
                itemsIndexed(cartItems) { index, item ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = item.product.imageDrawableRes ?: R.drawable.ic_pottery_logo),
                                contentDescription = item.product.nameEn,
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) item.product.nameHi else item.product.nameEn,
                                    color = DarkChocolate,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${item.selectedSize.sizeNameEn} • ${item.selectedColor.nameEn}",
                                    color = MutedText,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "₹${item.unitPrice.toInt()} x ${item.quantity} = ₹${(item.unitPrice * item.quantity).toInt()}",
                                    color = TerracottaDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            // Stepper
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(WarmCreamSurface, RoundedCornerShape(6.dp))
                                    .border(1.dp, CardBorderGold, RoundedCornerShape(6.dp))
                            ) {
                                IconButton(
                                    onClick = { onUpdateQuantity(index, item.quantity - 1) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Decrease",
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = "${item.quantity}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                IconButton(
                                    onClick = { onUpdateQuantity(index, item.quantity + 1) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase",
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Delivery Address Section
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Address",
                                    tint = Terracotta,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.HINDI) "डिलीवरी का पता (Delivery Address):" else "Delivery Details:",
                                    color = DarkChocolate,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = customerName,
                                onValueChange = { customerName = it },
                                label = { Text(if (language == AppLanguage.HINDI) "ग्राहक का नाम" else "Full Name", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = customerPhone,
                                onValueChange = { customerPhone = it },
                                label = { Text(if (language == AppLanguage.HINDI) "मोबाइल नंबर" else "Phone Number", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = deliveryAddress,
                                onValueChange = { deliveryAddress = it },
                                label = { Text(if (language == AppLanguage.HINDI) "पूरा पता व पिनकोड" else "Full Address & PIN", fontSize = 11.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }

                // Payment Method Selector (Requirement 3A.12)
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "भुगतान विकल्प (Payment Method):" else "Payment Method:",
                                color = DarkChocolate,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            PaymentMethod.values().forEach { method ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedPayment = method }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedPayment == method,
                                        onClick = { selectedPayment = method },
                                        colors = RadioButtonDefaults.colors(selectedColor = Terracotta)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.HINDI) method.titleHi else method.titleEn,
                                        color = DarkChocolate,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // Bill Details
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "बिल का विवरण (Bill Summary):" else "Bill Details:",
                                color = DarkChocolate,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) "बर्तनों का कुल मूल्य" else "Pottery Items Subtotal",
                                    color = MutedText,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "₹${subtotal.toInt()}",
                                    color = DarkChocolate,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) "सुरक्षित मिट्टी डिलीवरी शुल्क" else "Fragile Clay Delivery",
                                    color = MutedText,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = if (deliveryFee == 0.0)
                                        (if (language == AppLanguage.HINDI) "मुफ़्त (FREE)" else "FREE")
                                    else "₹${deliveryFee.toInt()}",
                                    color = if (deliveryFee == 0.0) ForestGreen else DarkChocolate,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            }

                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBeige)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) "कुल देय राशि (Total Amount)" else "Total Payable",
                                    color = DarkChocolate,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "₹${total.toInt()}",
                                    color = TerracottaDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Place Order Bar
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.HINDI) "कुल राशि:" else "Total:",
                            color = MutedText,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "₹${total.toInt()}",
                            color = TerracottaDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Button(
                        onClick = {
                            onPlaceOrder(customerName, customerPhone, deliveryAddress, selectedPayment)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        modifier = Modifier
                            .height(46.dp)
                            .testTag("place_order_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Place Order",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "ऑर्डर प्लेस करें" else "Place Order",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
