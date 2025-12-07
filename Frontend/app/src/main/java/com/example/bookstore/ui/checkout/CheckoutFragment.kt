package com.example.bookstore.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.lifecycle.lifecycleScope
import com.example.bookstore.R
import com.example.bookstore.ui.theme.BookstoreTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.bookstore.model.checkout.CheckoutUIModel
import com.example.bookstore.model.checkout.CheckoutUIProductItem
import com.example.bookstore.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.ui.text.style.TextAlign

class CheckoutFragment : Fragment(R.layout.fragment_checkout) {
    private val args: CheckoutFragmentArgs by navArgs()
    private lateinit var viewModel: CheckoutViewModel
    private lateinit var cartItemIds: List<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.composeView)

        cartItemIds = args.cartItemIds.toList()
        viewModel = ViewModelProvider(this, CheckoutViewModelFactory(requireContext()))
            .get(CheckoutViewModel::class.java)

        if (cartItemIds.isNotEmpty()) {
            viewModel.loadCheckoutReview(cartItemIds)
        } else {
            // Xử lý trường hợp không có ID nào được truyền (rất hiếm nếu từ Cart qua)
            // viewModel.uiState.value = viewModel.uiState.value.copy(errorMessage = "Không có sản phẩm để thanh toán.") // Cần cập nhật state nếu muốn hiển thị lỗi ngay
        }

        composeView.setContent {
            BookstoreTheme {
                val uiState by viewModel.uiState
                val reviewData = uiState.reviewData

                val navController = findNavController()
                LaunchedEffect(uiState.orderSuccess) {
                    if (uiState.orderSuccess) {
                        // Navigation sang màn hình Order và xóa màn hình Cart/Checkout khỏi backstack
                        navController.navigate(CheckoutFragmentDirections.actionCheckoutToOrder())
                    }
                }

                var selectedDelivery by remember {
                    mutableStateOf(DeliveryOption.TIET_KIEM)
                }

                LaunchedEffect(reviewData?.shippingType) {
                    reviewData?.shippingType?.let { type ->
                        selectedDelivery = DeliveryOption.values().find {
                            it.apiType == type
                        } ?: DeliveryOption.TIET_KIEM
                    }
                }

                CheckoutScreen(
                    reviewData = reviewData,
                    onBackClick = { findNavController().popBackStack() },
                    onOrder = {
                        // Gọi placeOrder với các ID cần thiết
                        if (reviewData != null) {
                            viewModel.placeOrder(
                                addressId = reviewData.addressId,
                                shippingMethodId = selectedDelivery.apiId, // Lấy từ trạng thái local Delivery
                                couponId = reviewData.couponCode
                            )
                        }
                    },
                    isLoading = uiState.isLoadingReview,
                    isPlacingOrder = uiState.isPlacingOrder,
                    errorMessage = uiState.errorMessage,
                    selectedDelivery = selectedDelivery,
                    onSelectDelivery = { selectedDelivery = it },
                    onRetry = {
                        if (cartItemIds.isNotEmpty()) {
                            viewModel.loadCheckoutReview(cartItemIds)
                        }
                    }
                )
            }
        }
    }
}

fun Double.toCurrencyString(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(this).replace("₫", " VNĐ").trim()
}

enum class DeliveryOption(val apiId: String, val apiType: String, val title: String, val price: Double, val time: String) {
    NHANH("fast_id", "fast", "Giao hàng nhanh", 30000.0, "1-2 ngày"),
    TIET_KIEM("standard_id", "standard", "Giao hàng tiết kiệm", 15000.0, "3-5 ngày")
}

// Composable
@Composable
fun CheckoutScreen(
    reviewData: CheckoutUIModel?,
    onBackClick: () -> Unit,
    onOrder: () -> Unit,
    isLoading: Boolean,
    isPlacingOrder: Boolean,
    errorMessage: String?,
    selectedDelivery: DeliveryOption,
    onSelectDelivery: (DeliveryOption) -> Unit,
    onRetry: () -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Header(onBackClick)

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize().weight(1f).padding(16.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Lỗi tải thông tin: $errorMessage", color = Color.Red, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onRetry) {
                        Text("Thử lại")
                    }
                }
            }
        } else if (reviewData != null) {

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item { AddressDisplay(reviewData.recipientName, reviewData.phoneNumber, reviewData.fullAddress) }
                item { OrderList(reviewData.productItems) }
                item { Delivery(selected = selectedDelivery, onSelect = onSelectDelivery) }
            }

            Footer(
                books = reviewData.productItems,
                deliveryFee = selectedDelivery.price,
                discount = reviewData.couponDiscountValue,
                onOrder = onOrder,
                isPlacingOrder = isPlacingOrder
            )
        }
    }
}

@Composable
fun Header(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFB0AEE0))
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.arrow_back_icon),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.align(Alignment.TopStart)
            )
        }
        Text(
            text = "XÁC NHẬN",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 4.dp)
        )
    }
}

@Composable
fun AddressDisplay(name: String, phone: String, fullAddress: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFEFEFEF), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = "Địa chỉ giao hàng",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text("Người nhận: $name - $phone", fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Địa chỉ: $fullAddress", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun AddressInput() {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address1 by remember { mutableStateOf("") }
    var address2 by remember { mutableStateOf("") }
    var address3 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Địa chỉ giao hàng",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val textFieldColors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        )

        Row(horizontalArrangement = Arrangement.Center) {
            TextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Họ và tên") },
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier.weight(2f)
            )
            TextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = { Text("SĐT") },
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        TextField(
            value = address1,
            onValueChange = { address1 = it },
            placeholder = { Text("Địa chỉ nhà") },
            colors = textFieldColors,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        TextField(
            value = address2,
            onValueChange = { address2 = it },
            placeholder = { Text("Xã/Phường") },
            colors = textFieldColors,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        TextField(
            value = address3,
            onValueChange = { address3 = it },
            placeholder = { Text("Tỉnh/Thành phố") },
            colors = textFieldColors,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
    }
}

@Composable
fun OrderCard(orderBook: CheckoutUIProductItem, modifier: Modifier = Modifier) {
    val totalItemPrice = orderBook.unitPrice * orderBook.quantity

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF2ED)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = orderBook.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(70.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = orderBook.categoryName, color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = orderBook.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 2.dp),
                    maxLines = 1
                )
                Text(text = orderBook.authorName, color = Color.Gray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SL: ${orderBook.quantity}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFD9D9D9), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = totalItemPrice.toCurrencyString(),
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderList(orderBooks: List<CheckoutUIProductItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Sản phẩm (${orderBooks.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            orderBooks.forEach { book ->
                OrderCard(orderBook = book)
            }
        }
    }
}

@Composable
fun Delivery(selected: DeliveryOption, onSelect: (DeliveryOption) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Hình thức giao hàng", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        DeliveryOption.values().forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(option) }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selected == option,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFF5ADBC),
                        unselectedColor = Color.LightGray
                    )
                )
                Spacer(Modifier.width(12.dp))
                Text(option.title, fontSize = 16.sp)
                Spacer(Modifier.weight(1f))
                Text(text = option.price.toCurrencyString(), color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun Footer(
    books: List<CheckoutUIProductItem>,
    deliveryFee: Double,
    discount: Double,
    onOrder: () -> Unit,
    isPlacingOrder: Boolean
) {
    val subtotal = books.sumOf { it.unitPrice * it.quantity }
    val totalDiscount = discount
    val finalTotal = subtotal + deliveryFee - totalDiscount

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Chi tiết tổng tiền
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Tổng tiền hàng:", fontSize = 14.sp, color = Color.Gray)
            Text(subtotal.toCurrencyString(), fontSize = 14.sp, color = Color.Gray)
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Phí vận chuyển:", fontSize = 14.sp, color = Color.Gray)
            Text(deliveryFee.toCurrencyString(), fontSize = 14.sp, color = Color.Gray)
        }
        if (totalDiscount > 0) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Giảm giá Coupon:", fontSize = 14.sp, color = Color.Red)
                Text("- ${totalDiscount.toCurrencyString()}", fontSize = 14.sp, color = Color.Red)
            }
        }

        Spacer(Modifier.height(8.dp))
        Divider()
        Spacer(Modifier.height(8.dp))

        // Tổng thanh toán cuối cùng
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Tổng thanh toán:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.weight(1f))
            Text(
                finalTotal.toCurrencyString(),
                color = Color(0xFFF5ADBC),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onOrder,
            enabled = finalTotal > 0 && !isPlacingOrder,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5ADBC)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            if (isPlacingOrder) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    "ĐẶT HÀNG",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

//fun calculateTotal(books: List<CheckoutBook>, deliveryPrice: Int): Int {
//    val booksTotal = books.sumOf { (it.price.replace(".", "").toIntOrNull() ?: 0) * it.quantity }
//    return booksTotal + deliveryPrice
//}
//
//// Previews
//@Preview
//@Composable
//fun HeaderPreview() { BookstoreTheme { Header { } } }
//
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun AddressPreview() { BookstoreTheme { AddressInput() } }
//
//@Preview(showBackground = true)
//@Composable
//fun OrderCardPreview() {
//    val sample = CheckoutBook(R.drawable.book1, "Sách Truyền cảm hứng", "Muôn kiếp nhân sinh", "Nguyễn Phong", "99.000đ", 1)
//    BookstoreTheme { Surface { OrderCard(sample) } }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun OrderListPreview() {
//    val sampleList = listOf(
//        CheckoutBook(R.drawable.book1, "Sách Truyền cảm hứng", "Muôn kiếp nhân sinh", "Nguyễn Phong", "99.000đ", 1),
//        CheckoutBook(R.drawable.book2, "Sách Thiếu nhi", "Cho tôi xin một vé đi tuổi thơ", "Nguyễn Nhật Ánh", "69.000đ", 2)
//    )
//    BookstoreTheme { Surface { OrderList(sampleList) } }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DeliveryPreview() {
//    BookstoreTheme {
//        var selected by remember { mutableStateOf(DeliveryOption.TIET_KIEM) }
//        Delivery(selected = selected, onSelect = { selected = it })
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun FooterPreview() {
//    val sampleBooks = listOf(
//        CheckoutBook(0, "Sách", "Lập trình Kotlin", "John Doe", "70000", 2)
//    )
//    BookstoreTheme { Footer(sampleBooks, DeliveryOption.TIET_KIEM, onOrder = {}) }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun CheckoutScreenPreview() {
//    val testBooks = listOf(
//        CheckoutBook(R.drawable.book4, "Sách", "Lập trình Kotlin", "John Doe", "150000", 2),
//        CheckoutBook(R.drawable.book3, "Truyện", "Harry Potter", "J.K. Rowling", "140000", 1)
//    )
//    BookstoreTheme { CheckoutScreen(testBooks, {}, {}, false) }
//}