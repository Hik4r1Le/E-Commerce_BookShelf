package com.example.bookstore.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.network.ApiService
import com.example.bookstore.repository.CartRepository
import com.example.bookstore.ui.theme.BookstoreTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.example.bookstore.model.cart.CartUIModel
import java.text.NumberFormat
import java.util.Locale
import com.example.bookstore.R

class CartFragment : Fragment(R.layout.fragment_cart) {
    private val viewModel: CartViewModel by viewModels() {
        CartViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.composeView)

        viewModel.loadCartDetails()

        composeView.setContent {
            BookstoreTheme {

                // Lấy toàn bộ trạng thái từ ViewModel
                val uiState by viewModel.uiState

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CartScreen(
                        cartItems = uiState.cartItems,
                        isLoading = uiState.isLoading,
                        errorMessage = uiState.errorMessage,
                        // Tính toán tổng tiền/tổng món qua hàm trong ViewModel
                        totalPrice = viewModel.getTotalPrice(),
                        totalItems = viewModel.getTotalItems(),
                        onBackClick = {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        },
                        onCheckoutClick = {
                        /* TODO: Điều hướng qua Checkout */
                            val checkedIds = viewModel.getCheckedCartItemIds()
                            if (checkedIds.isNotEmpty()) {
                                findNavController().navigate(CartFragmentDirections.actionCartToCheckout(checkedIds))
                            } else {
                                // TODO: Hiển thị Toast/Snackbar thông báo "Vui lòng chọn sản phẩm"
                                // Ví dụ: Toast.makeText(context, "Vui lòng chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onCheckedChange = { id, checked ->
                            viewModel.toggleChecked(id, checked)
                        },
                        onQuantityChange = { id, qty ->
                            viewModel.updateQuantity(id, qty)
                        },
                        onDelete = { id ->
                            viewModel.removeItem(id)
                        },
                        onRetry = {
                            viewModel.loadCartDetails()
                        }
                    )
                }
            }
        }
    }
}

// HÀM EXTENSION ĐỊNH DẠNG TIỀN TỆ (Giữ nguyên)
fun Double.toCurrencyString(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(this).replace("₫", " VNĐ").trim()
}

// -------------------------------------------------------------------------------------------------
// 3. CartScreen (Cập nhật tham số)
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartUIModel>, // Sử dụng CartUIModel
    isLoading: Boolean,
    errorMessage: String?,
    totalPrice: Double, // Đã đổi sang Double
    totalItems: Int,
    onBackClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {},
    onCheckedChange: (String, Boolean) -> Unit = { _, _ -> }, // ID là String
    onQuantityChange: (String, Int) -> Unit = { _, _ -> }, // ID là String
    onDelete: (String) -> Unit = {}, // ID là String
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F3))
    ) {

        // Header (Giữ nguyên)
        Row(
            // ... (Code Header)
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


        // ------------------ CONTENT (Xử lý Loading / Error / Data) ------------------

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Column(
                modifier = Modifier.fillMaxSize().weight(1f).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Lỗi tải dữ liệu: $errorMessage", color = Color.Red, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry) {
                    Text("Thử lại")
                }
            }
        } else if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text("Giỏ hàng trống", style = MaterialTheme.typography.headlineMedium, color = Color.Gray)
            }
        } else {
            // Cart items
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems, key = { it.cartItemId }) { item ->
                    CartItemCard(
                        item = item,
                        onCheckedChange = { checked -> onCheckedChange(item.cartItemId, checked) },
                        onQuantityChange = { qty -> onQuantityChange(item.cartItemId, qty) },
                        onDelete = { onDelete(item.cartItemId) }
                    )
                }
            }
        }

        // Footer (Giữ nguyên)
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
                    totalPrice.toCurrencyString(),
                    fontSize = 20.sp,
                    color = Color.Red
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onCheckoutClick,
                enabled = totalItems > 0 && !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2AEBB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Thanh toán (${totalItems} món)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 4. CartItemCard (Cập nhật tham số)
// -------------------------------------------------------------------------------------------------
@Composable
fun CartItemCard(
    item: CartUIModel, // Sử dụng CartUIModel
    onCheckedChange: (Boolean) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    // Tính toán giá sau giảm
    val discountedPrice = item.price * (1.0 - item.discount / 100.0) // Giả sử discount là %

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
                checked = item.checked, // Lấy trạng thái checked từ CartUIModel
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 8.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFA7AAE1),
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )

            // Sử dụng AsyncImage cho ảnh từ URL
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.productName,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(item.productName, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 2)
                    Text(item.authorName, fontSize = 14.sp, color = Color.Gray, maxLines = 2)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        discountedPrice.toCurrencyString(), // Hiển thị giá sau giảm
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