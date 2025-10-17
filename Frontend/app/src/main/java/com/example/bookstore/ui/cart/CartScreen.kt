package com.example.bookstore.ui.cart

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstore.R

// Model dữ liệu giỏ hàng
data class CartItemData(
    val id: Int,
    val name: String,
    val author: String,
    val price: Int,
    var quantity: Int,
    val resId: Int
)

@Composable
fun CartScreen(
    cartItems: MutableList<CartItemData>,
    onNavigateBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val totalPrice by derivedStateOf { cartItems.sumOf { it.price * it.quantity } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopAppBar(
            title = { Text("Giỏ hàng") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                Box(modifier = Modifier.padding(end = 16.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shopping_cart),
                        contentDescription = "Cart",
                        modifier = Modifier.size(28.dp)
                    )
                    if (cartItems.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color.Red, shape = RoundedCornerShape(8.dp))
                                .align(Alignment.TopEnd)
                        ) {
                            Text(
                                text = "${cartItems.size}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            },
            backgroundColor = Color.White,
            contentColor = Color.Black,
            elevation = 4.dp
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cartItems, key = { it.id }) { item ->
                var quantity by remember { mutableStateOf(item.quantity) }

                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = true,
                            onCheckedChange = { /* TODO */ }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = item.resId),
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.LightGray, RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(item.author, fontSize = 14.sp, color = Color.Gray)
                            Text("${item.price}₫", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                if (quantity > 1) quantity--
                                item.quantity = quantity
                            }) { Text("-", fontSize = 20.sp) }
                            Text(
                                "$quantity",
                                fontSize = 16.sp,
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.Center
                            )
                            IconButton(onClick = {
                                quantity++
                                item.quantity = quantity
                            }) { Text("+", fontSize = 20.sp) }
                        }
                        IconButton(onClick = { cartItems.remove(item) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa")
                        }
                    }
                }
            }
        }

        Card(
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Tổng cộng", fontSize = 14.sp, color = Color.Gray)
                    Text("$totalPrice₫", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Button(onClick = onCheckout) {
                    Text("Mua hàng")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    CartScreen(
        cartItems = mutableStateListOf(
            CartItemData(1, "Sách A", "Tác giả A", 100000, 1, R.drawable.book1),
            CartItemData(2, "Sách B", "Tác giả B", 85000, 2, R.drawable.book2),
            CartItemData(3, "Sách C", "Tác giả C", 120000, 1, R.drawable.book3)
        ),
        onNavigateBack = {},
        onCheckout = {}
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartScreenPreviewDark() {
    CartScreen(
        cartItems = mutableStateListOf(
            CartItemData(1, "Sách A", "Tác giả A", 100000, 1, R.drawable.book1),
            CartItemData(2, "Sách B", "Tác giả B", 85000, 2, R.drawable.book2)
        ),
        onNavigateBack = {},
        onCheckout = {}
    )
}
