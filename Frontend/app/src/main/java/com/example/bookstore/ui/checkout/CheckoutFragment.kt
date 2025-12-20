package com.example.bookstore.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.example.bookstore.R
import com.example.bookstore.model.checkout.AddressUIItem
import com.example.bookstore.model.checkout.CheckoutUIProductItem
import com.example.bookstore.model.checkout.CouponDetail
import com.example.bookstore.model.checkout.ShippingDetail
import com.example.bookstore.ui.theme.BookstoreTheme
import java.text.NumberFormat
import java.util.*

class CheckoutFragment : Fragment(R.layout.fragment_checkout) {
    private val args: CheckoutFragmentArgs by navArgs()
    private lateinit var viewModel: CheckoutViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.composeView)

        val cartItemIds = args.cartItemIds.toList()
        viewModel = ViewModelProvider(this, CheckoutViewModelFactory(requireContext()))
            .get(CheckoutViewModel::class.java)

        if (cartItemIds.isNotEmpty()) {
            viewModel.loadCheckoutReview(cartItemIds)
        }

        composeView.setContent {
            BookstoreTheme {
                val uiState by viewModel.uiState
                val reviewData = uiState.reviewData
                val navController = findNavController()

                LaunchedEffect(uiState.orderSuccess) {
                    if (uiState.orderSuccess) {
                        navController.navigate(CheckoutFragmentDirections.actionCheckoutToOrder())
                    }
                }

                CheckoutScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

// Helper định dạng tiền tệ
fun Double.toCurrencyString(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(this).replace("₫", " VNĐ").trim()
}

@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState
    val reviewData = uiState.reviewData

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9)).navigationBarsPadding()) {
        // 1. Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFB0AEE0))
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Xác nhận đơn hàng",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (uiState.isLoadingReview) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFB0AEE0))
            }
        } else if (uiState.errorMessage != null) {
            Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                Text(uiState.errorMessage!!, color = Color.Red, textAlign = TextAlign.Center)
            }
        } else if (reviewData != null) {
            LazyColumn(modifier = Modifier.weight(1f)) {

                // 2. Address Section
                item {
                    AddressSection(
                        addresses = reviewData.addresses,
                        uiState = uiState,
                        onAddressSelected = { viewModel.onAddressSelected(it) },
                        onNameChange = { viewModel.onNameChange(it) },
                        onPhoneChange = { viewModel.onPhoneChange(it) },
                        onStreetChange = { viewModel.onStreetChange(it) },
                        onDistrictChange = { viewModel.onDistrictChange(it) },
                        onCityChange = { viewModel.onCityChange(it) }
                    )
                }

                // 3. Product List
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Sản phẩm (${reviewData.productItems.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.height(8.dp))
                        reviewData.productItems.forEach { item ->
                            OrderCard(item)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }

                // 4. Coupon Section
                item {
                    CouponSection(
                        coupons = reviewData.coupons,
                        selectedId = uiState.selectedCouponId,
                        onSelect = { viewModel.onCouponSelected(it) }
                    )
                }

                // 5. Shipping Section
                item {
                    DeliverySection(
                        methods = reviewData.shippingMethods,
                        selectedId = uiState.selectedShippingId,
                        onSelect = { viewModel.onShippingSelected(it) }
                    )
                }
            }

            // 6. Footer với logic tính toán chính xác
            val selectedShipping = reviewData.shippingMethods.find { it.id == uiState.selectedShippingId }
            val selectedCoupon = reviewData.coupons.find { it.id == uiState.selectedCouponId }

            Footer(
                productItems = reviewData.productItems,
                deliveryFee = selectedShipping?.shippingFee ?: 0.0,
                couponDiscount = selectedCoupon?.discountValue ?: 0.0,
                isPlacingOrder = uiState.isPlacingOrder,
                onOrder = { viewModel.placeOrder() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSection(
    addresses: List<AddressUIItem>,
    uiState: CheckoutUiState,
    onAddressSelected: (AddressUIItem) -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onStreetChange: (String) -> Unit,
    onDistrictChange: (String) -> Unit,
    onCityChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp).background(Color.White, RoundedCornerShape(12.dp)).padding(16.dp)) {
        Text("Địa chỉ nhận hàng", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFB0AEE0))
        Spacer(Modifier.height(12.dp))

        if (addresses.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = if (uiState.selectedAddressId != null) "Dùng địa chỉ đã lưu" else "Nhập địa chỉ mới",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Lựa chọn địa chỉ") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    addresses.forEach { addr ->
                        DropdownMenuItem(
                            text = {
                                // Nối chuỗi chỉ để hiển thị trên menu chọn
                                Text("${addr.label}: ${addr.street}, ${addr.district}, ${addr.city}")
                            },
                            onClick = {
                                onAddressSelected(addr)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        OutlinedTextField(value = uiState.recipientName, onValueChange = onNameChange, label = { Text("Họ tên") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = uiState.phoneNumber, onValueChange = onPhoneChange, label = { Text("SĐT") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = uiState.street, onValueChange = onStreetChange, label = { Text("Số nhà, tên đường") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(value = uiState.district, onValueChange = onDistrictChange, label = { Text("Quận/Huyện") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(value = uiState.city, onValueChange = onCityChange, label = { Text("Tỉnh/Thành") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
        }
    }
}

@Composable
fun OrderCard(item: CheckoutUIProductItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.productName, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(item.authorName, fontSize = 13.sp, color = Color.Gray)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("x${item.quantity}", fontSize = 14.sp)
                    Text(item.unitPrice.toCurrencyString(), color = Color(0xFFF5ADBC), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CouponSection(coupons: List<CouponDetail>, selectedId: String?, onSelect: (String?) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Ưu đãi từ Shop", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        if (coupons.isEmpty()) {
            Text("Không có mã giảm giá", color = Color.Gray, fontSize = 14.sp)
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(coupons) { coupon ->
                    FilterChip(
                        selected = selectedId == coupon.id,
                        onClick = { if (selectedId == coupon.id) onSelect(null) else onSelect(coupon.id) },
                        label = { Text(coupon.code) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFF5ADBC))
                    )
                }
            }
        }
    }
}

@Composable
fun DeliverySection(methods: List<ShippingDetail>, selectedId: String?, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Vận chuyển", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        methods.forEach { method ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable { onSelect(method.id) }.padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedId == method.id,
                    onClick = { onSelect(method.id) },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF5ADBC))
                )
                Column(Modifier.weight(1f)) {
                    Text(method.type, fontWeight = FontWeight.Medium)
                    Text(method.estimatedTimeText, fontSize = 12.sp, color = Color.Gray)
                }
                Text(method.shippingFee.toCurrencyString(), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun Footer(
    productItems: List<CheckoutUIProductItem>,
    deliveryFee: Double,
    couponDiscount: Double,
    isPlacingOrder: Boolean,
    onOrder: () -> Unit
) {
    val totalProducts = productItems.sumOf { it.unitPrice * it.quantity }
    val finalTotal = (totalProducts + deliveryFee - couponDiscount).coerceAtLeast(0.0)

    Surface(
        shadowElevation = 16.dp,
        modifier = Modifier.padding(bottom = 90.dp)
    ) {
        Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Tiền hàng", color = Color.Gray)
                Text(totalProducts.toCurrencyString())
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Phí giao hàng", color = Color.Gray)
                Text("+ ${deliveryFee.toCurrencyString()}")
            }
            if (couponDiscount > 0) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Giảm giá", color = Color.Red)
                    Text("- ${couponDiscount.toCurrencyString()}", color = Color.Red)
                }
            }
            Divider(Modifier.padding(vertical = 12.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text("Tổng thanh toán", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(finalTotal.toCurrencyString(), color = Color(0xFFF5ADBC), fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onOrder,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5ADBC)),
                shape = RoundedCornerShape(12.dp),
                enabled = !isPlacingOrder
            ) {
                if (isPlacingOrder) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("ĐẶT HÀNG", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}