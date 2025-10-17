package com.example.bookstore.ui.cart

import android.content.res.Configuration
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstore.R
import com.example.bookstore.ui.theme.BookstoreTheme

// Dữ liệu mẫu
data class CartItem(
    val id: Int,
    val name: String,
    val author: String,
    val price: Int,
    val imageRes: Int
)

fun sampleCartItems() = listOf(
    CartItem(1, "Tâm Lý Học & Đời Sống", "Richard J. Gerrig & Philip G. Zimbardo", 95000, R.drawable.book1),
    CartItem(2, "Memoirs", "Kanika Sharma", 88000, R.drawable.book2),
    CartItem(3, "Đắc Nhân Tâm", "Dale Carnegie", 120000, R.drawable.book3),
    CartItem(4, "Kính Vạn Hoa", "Nguyễn Nhật Ánh", 86000, R.drawable.book4)
)

// Cart Screen 
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {}
) {
    val cartItems = remember { sampleCartItems().toMutableStateList() }

    val quantities = remember(cartItems) {
        mutableStateMapOf<Int, Int>().apply {
            cartItems.forEach {
                val defaultQty = when (it.name) {
                    "Memoirs" -> 2
                    "Kính Vạn Hoa" -> 3
                    else -> 1
                }
                put(it.id, defaultQty)
            }
        }
    }

    val checkedStates = remember(cartItems) {
        mutableStateMapOf<Int, Boolean>().apply { cartItems.forEach { put(it.id, true) } }
    }

    val totalPrice by remember {
        derivedStateOf {
            cartItems.filter { checkedStates[it.id] == true }
                .sumOf { it.price * (quantities[it.id] ?: 1) }
        }
    }

    val totalItems by remember {
        derivedStateOf {
            cartItems.filter { checkedStates[it.id] == true }
                .sumOf { quantities[it.id] ?: 1 }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F3))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFFA7AAE1))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.DarkGray)
            }
            Text("Giỏ hàng", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Box(contentAlignment = Alignment.TopEnd) {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = "Cart",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(32.dp)
                )
                if (totalItems > 0) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(Color.Red, shape = CircleShape)
                            .align(Alignment.TopEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$totalItems", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Danh sách sản phẩm
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(cartItems) { _, item ->
                val quantity = quantities[item.id] ?: 1
                CartItemCard(
                    item = item,
                    quantity = quantity,
                    checked = checkedStates[item.id] == true,
                    onCheckedChange = { checkedStates[item.id] = it },
                    onQuantityChange = { newQty -> quantities[item.id] = newQty },
                    onDelete = {
                        cartItems.remove(item)
                        quantities.remove(item.id)
                        checkedStates.remove(item.id)
                    }
                )
            }
        }

        // Nút thanh toán
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF8F3))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tổng cộng:", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                Text("${"%,d".format(totalPrice)} đ", fontSize = 20.sp, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onCheckoutClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2AEBB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Thanh toán", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// Card item
@Composable
fun CartItemCard(
    item: CartItem,
    quantity: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(4.dp, Color(0xFFF2AEBB), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5D3C4)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 8.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFA7AAE1),
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )

            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 2)
                    Text(item.author, fontSize = 14.sp, color = Color.Gray, maxLines = 2)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${"%,d".format(item.price)} đ", fontSize = 16.sp, color = Color.Red)
                }

                Row(
                    modifier = Modifier
                        .width(120.dp)
                        .height(36.dp)
                        .background(Color(0xFFD9D9D9), RoundedCornerShape(8.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { if (quantity > 1) onQuantityChange(quantity - 1) }
                            .wrapContentSize(Alignment.Center)
                    ) { Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold) }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color.Gray)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    ) { Text("$quantity", fontSize = 16.sp, fontWeight = FontWeight.Medium) }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color.Gray)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onQuantityChange(quantity + 1) }
                            .wrapContentSize(Alignment.Center)
                    ) { Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.Top)
            ) { Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Black) }
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun CartPreviewShort() {
    val items = sampleCartItems().take(3)
    BookstoreTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .border(4.dp, Color(0xFFF2AEBB), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5D3C4)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize()
            ) {
                items.forEach { item ->
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartPreviewMedium() {
    val items = sampleCartItems().take(3)
    val quantities = remember {
        mutableStateListOf<Int>().apply {
            items.forEach {
                val defaultQty = when (it.name) {
                    "Memoirs" -> 2
                    "Kính Vạn Hoa" -> 3
                    else -> 1
                }
                add(defaultQty)
            }
        }
    }

    BookstoreTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(items) { index, item ->
                CartItemCard(
                    item = item,
                    quantity = quantities[index],
                    checked = true,
                    onCheckedChange = {},
                    onQuantityChange = { newQty -> quantities[index] = newQty },
                    onDelete = {}
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartPreviewFull() {
    BookstoreTheme {
        CartScreen()
    }
}
