package com.example.bookstore.ui.cart

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bookstore.R
import com.example.bookstore.api.cart.CartApi
import com.example.bookstore.api.cart.CartRepository
import com.example.bookstore.ui.theme.BookstoreTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartFragment : Fragment() {

    private val viewModel: CartViewModel by lazy {

        // Tạo Retrofit (sửa lại baseURL đúng API của bạn)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://your-api.com/") // TODO: đổi lại base url thật
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(CartApi::class.java)
        val repo = CartRepository(api)

        ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return CartViewModel(repo) as T
            }
        })[CartViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {

        setContent {
            BookstoreTheme {

                val items by viewModel.cartItems.collectAsStateWithLifecycle()
                val totalPrice = viewModel.getTotalPrice()
                val totalItems = viewModel.getTotalItems()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CartScreen(
                        cartItems = items,
                        totalPrice = totalPrice,
                        totalItems = totalItems,
                        onBackClick = {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        },
                        onCheckoutClick = { /* TODO: Điều hướng qua Checkout */ },
                        onCheckedChange = { id, checked ->
                            viewModel.toggleChecked(id, checked)
                        },
                        onQuantityChange = { id, qty ->
                            viewModel.updateQuantity(id, qty)
                        },
                        onDelete = { id ->
                            viewModel.removeItem(id)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartViewModel.CartItem>,
    totalPrice: Int,
    totalItems: Int,
    onBackClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {},
    onCheckedChange: (Int, Boolean) -> Unit = { _, _ -> },
    onQuantityChange: (Int, Int) -> Unit = { _, _ -> },
    onDelete: (Int) -> Unit = {}
) {
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
                        Text(
                            "$totalItems",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Cart items
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cartItems, key = { it.id }) { item ->
                CartItemCard(
                    item = item,
                    onCheckedChange = { checked -> onCheckedChange(item.id, checked) },
                    onQuantityChange = { qty -> onQuantityChange(item.id, qty) },
                    onDelete = { onDelete(item.id) }
                )
            }
        }

        // Footer
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
                Text(
                    "${"%,d".format(totalPrice)} đ",
                    fontSize = 20.sp,
                    color = Color.Red
                )
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
                Text(
                    "Thanh toán",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartViewModel.CartItem,
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
                checked = item.checked,
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
                    Text(
                        "${"%,d".format(item.price)} đ",
                        fontSize = 16.sp,
                        color = Color.Red
                    )
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
                            .clickable { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }
                            .wrapContentSize(Alignment.Center)
                    ) { Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold) }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    ) { Text("${item.quantity}", fontSize = 16.sp, fontWeight = FontWeight.Medium) }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onQuantityChange(item.quantity + 1) }
                            .wrapContentSize(Alignment.Center)
                    ) { Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Black)
            }
        }
    }
}

// Previews

fun previewSampleCartItems() = listOf(
    CartViewModel.CartItem(1, "Tâm Lý Học & Đời Sống", "Richard J. Gerrig & Philip G. Zimbardo", 95_000, R.drawable.book1, quantity = 1),
    CartViewModel.CartItem(2, "Memoirs", "Kanika Sharma", 88_000, R.drawable.book2, quantity = 2),
    CartViewModel.CartItem(3, "Đắc Nhân Tâm", "Dale Carnegie", 120_000, R.drawable.book3, quantity = 1),
)

@Preview(showBackground = true)
@Composable
fun CartPreviewShort() {
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
                previewSampleCartItems().forEach { item ->
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
    val items = previewSampleCartItems()
    val quantities = remember { mutableStateListOf(1, 2, 1) }

    BookstoreTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                val idx = items.indexOf(item)
                CartItemCard(
                    item = item.copy(quantity = quantities[idx]),
                    onCheckedChange = {},
                    onQuantityChange = { newQty -> quantities[idx] = newQty },
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
        CartScreen(
            cartItems = previewSampleCartItems(),
            totalPrice = previewSampleCartItems().sumOf { it.price * it.quantity },
            totalItems = previewSampleCartItems().sumOf { it.quantity },
            onBackClick = {},
            onCheckoutClick = {},
            onCheckedChange = { _, _ -> },
            onQuantityChange = { _, _ -> },
            onDelete = {}
        )
    }
}