package com.example.bookstore.ui.order

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.R
import com.example.bookstore.model.order.OrderUIModel
import com.example.bookstore.network.ApiService
import com.example.bookstore.repository.OrderRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.Locale
import coil.compose.AsyncImage

private val TealGreen = Color(0xFF17A590)
private val GrayText = Color(0xFF757575)
private val GrayArrow = Color(0xFFBDBDBD)
private val PinkBtn = Color(0xFFF2AEBB)
private val SuccessPriceColor = Color(0xFF2AD549)

// HÀM ĐỊNH DẠNG TIỀN TỆ
fun Double.toCurrencyString(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(this).replace("₫", " VNĐ").trim()
}

class OrderFragment : Fragment(R.layout.fragment_order) {

    private lateinit var viewModel: OrderViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.composeView)

        viewModel = ViewModelProvider(this, OrderViewModelFactory(requireContext()))
                .get(OrderViewModel::class.java)

        viewModel.loadOrders()

        composeView.setContent {
            val uiState by viewModel.uiState

            OrdersContent(
                tabs = viewModel.tabs,
                selectedTabIndex = uiState.selectedTabIndex,
                totalMessages = uiState.totalMessages,
                orderSections = uiState.orderSections,
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                onTabSelected = { viewModel.onTabSelected(it) },
                onBackClick = { requireActivity().onBackPressedDispatcher.onBackPressed() },
                onMessageClick = { viewModel.clearMessages() }
            )
        }
    }
}

// UI Composables
@Composable
fun OrdersHeader(totalMessages: Int, onBackClick: () -> Unit, onMessageClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFF8E8CD8))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Text(
                text = "ĐƠN MUA",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = onMessageClick) { Icon(Icons.Filled.Chat, contentDescription = "Chat", tint = Color.White, modifier = Modifier.size(28.dp)) }
                if (totalMessages > 0) {
                    Box(
                        modifier = Modifier.size(16.dp).background(Color.Red, shape = CircleShape).align(Alignment.TopEnd),
                        contentAlignment = Alignment.Center
                    ) { Text("$totalMessages", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: OrderUIModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, PinkBtn, RoundedCornerShape(10.dp))
            .background(Color(0xFFF5D3C4), RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = order.imageUrl, // Sử dụng AsyncImage cho URL ảnh
            contentDescription = order.productName,
            modifier = Modifier.size(70.dp).background(Color.White, RoundedCornerShape(6.dp)).padding(4.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(order.productName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(order.authorName, fontSize = 12.sp, color = Color.Gray)
            Text("Số lượng: ${order.quantity}", fontSize = 12.sp)
            Text("Trạng thái: ${order.status}", fontSize = 12.sp, color = TealGreen)
        }
        Text(
            (order.unitPrice * (1 - order.discount / 100.0)).toCurrencyString(), // Giá sau giảm
            modifier = Modifier.background(Color.White, RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Composable
fun SectionOrderCard(section: OrderDataSection) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(6.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(section.status.label, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            // Danh sách Order Items
            section.orders.forEach { order ->
                OrderItem(order)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Ghi chú trạng thái (Tùy chỉnh logic hiển thị)
            val showNote = true // Giả định luôn hiển thị note
            if(showNote){
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    val note = when(section.status){
                        OrderStatus.PENDING -> "Đơn hàng đang chờ xác nhận."
                        OrderStatus.WAIT_PICKUP -> "Đơn hàng đang được chuẩn bị."
                        OrderStatus.WAIT_DELIVERY -> "Đơn hàng đã được vận chuyển."
                        OrderStatus.DELIVERED -> "Đã giao hàng thành công."
                        OrderStatus.CANCELED -> "Đơn hàng đã bị hủy."
                    }
                    if(note.isNotEmpty()){
                        Text(note, fontSize = 12.sp, color = TealGreen, modifier = Modifier.weight(1f))
                        Text(">", color = GrayArrow, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tổng tiền
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${section.orders.sumOf { it.quantity }} sản phẩm", color = GrayText, modifier = Modifier.padding(end = 8.dp))
                Text("Tổng thanh toán:", color = Color.Black, modifier = Modifier.padding(end = 8.dp))
                Text(
                    section.totalPrice.toCurrencyString(), // Sử dụng totalPrice từ Section
                    fontWeight = FontWeight.Bold,
                    color = SuccessPriceColor // Màu xanh lá cây
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Button Hành động
            if (section.actionText.isNotEmpty()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                    Box(
                        modifier = Modifier
                            .shadow(4.dp, RoundedCornerShape(8.dp))
                            .background(PinkBtn, RoundedCornerShape(8.dp))
                            .clickable {}
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(section.actionText, color = Color.White, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
fun OrdersContent(
    tabs: List<String>,
    selectedTabIndex: Int,
    totalMessages: Int,
    orderSections: List<OrderDataSection>,
    isLoading: Boolean,
    errorMessage: String?,
    onTabSelected: (Int) -> Unit,
    onBackClick: () -> Unit,
    onMessageClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5))) {
        OrdersHeader(totalMessages, onBackClick, onMessageClick)
        TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth(), containerColor = Color.White, contentColor = Color.Black) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTabIndex == index, onClick = { onTabSelected(index) }, modifier = Modifier.weight(1f)) {
                    Text(text = title, color = Color.Black, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.padding(vertical = 12.dp))
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                item {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
                    Text("Đang tải đơn hàng...", modifier = Modifier.padding(top = 16.dp))
                }
            } else if (errorMessage != null) {
                item {
                    Text("Lỗi: $errorMessage", color = Color.Red, modifier = Modifier.padding(32.dp))
                }
            } else if (orderSections.isNotEmpty()) {
                val currentSection = orderSections.getOrNull(selectedTabIndex)
                if (currentSection != null) {
                    item { SectionOrderCard(currentSection) }
                } else {
                    item { Text("Không tìm thấy đơn hàng nào trong mục này.", color = GrayText, modifier = Modifier.padding(32.dp)) }
                }
            } else {
                item { Text("Không có đơn hàng nào.", color = GrayText, modifier = Modifier.padding(32.dp)) }
            }
        }
    }
}

// Previews
//val sampleOrders = listOf(
//    OrderData(1,"Muôn kiếp nhân sinh","Nguyễn Phong",99,1,R.drawable.book6,OrderStatus.PENDING,"Đang xử lý"),
//    OrderData(2,"Cho tôi một vé đi tuổi thơ","Nguyễn Nhật Ánh",69,2,R.drawable.book7,OrderStatus.PENDING,"Đang xử lý")
//)
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewSectionWaitingConfirmation() {
//    SectionOrderCardFloat(OrderDataSection(OrderStatus.PENDING, sampleOrders, 257, "Đang xử lý"), showNote = false)
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewSectionWaitingPickup() {
//    SectionOrderCardFloat(OrderDataSection(OrderStatus.WAIT_PICKUP, sampleOrders, 257, "Liên hệ Shop"), showNote = true)
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewSectionWaitingDelivery() {
//    SectionOrderCardFloat(OrderDataSection(OrderStatus.WAIT_DELIVERY, sampleOrders, 257, "Đã nhận hàng"), showNote = true)
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewSectionDelivered() {
//    SectionOrderCardFloat(OrderDataSection(OrderStatus.DELIVERED, sampleOrders, 257, "Mua lại"), showNote = true)
//}
//
//@Preview(showBackground = true, heightDp = 520)
//@Composable
//fun PreviewOrdersScreen() {
//    var selectedTabIndex by remember { mutableStateOf(0) }
//    OrdersContent(
//        tabs = listOf("Chờ xác nhận","Chờ lấy hàng","Chờ giao hàng","Đã giao"),
//        selectedTabIndex = selectedTabIndex,
//        totalMessages = 8,
//        orderSections = listOf(
//            OrderDataSection(OrderStatus.PENDING, sampleOrders, 257, "Đang xử lý"),
//            OrderDataSection(OrderStatus.WAIT_PICKUP, sampleOrders, 257, "Liên hệ Shop"),
//            OrderDataSection(OrderStatus.WAIT_DELIVERY, sampleOrders, 257, "Đã nhận hàng"),
//            OrderDataSection(OrderStatus.DELIVERED, sampleOrders, 257, "Mua lại")
//        ),
//        onTabSelected = { selectedTabIndex = it },
//        onBackClick = {},
//        onMessageClick = {}
//    )
//}