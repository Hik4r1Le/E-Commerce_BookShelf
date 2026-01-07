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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.example.bookstore.R
import com.example.bookstore.model.order.OrderUIModel
import java.text.NumberFormat
import java.util.Locale

// --- ĐỊNH NGHĨA MÀU SẮC ---
private val TealGreen = Color(0xFF17A590)
private val GrayText = Color(0xFF757575)
private val PinkBtn = Color(0xFFF2AEBB)
private val SuccessPriceColor = Color(0xFF2AD549)
private val HeaderPurple = Color(0xFF8E8CD8)

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
                onBackClick = { findNavController().navigateUp() },
                onMessageClick = { viewModel.clearMessages() },
                // Gắn logic thực hiện hành động vào đây
                onActionClick = { orderId, status ->
                    viewModel.performOrderAction(orderId, status)
                }
            )
        }
    }
}

// --- UI COMPONENTS ---

@Composable
fun OrdersHeader(totalMessages: Int, onBackClick: () -> Unit, onMessageClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(HeaderPurple)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "ĐƠN MUA",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = onMessageClick) {
                    Icon(Icons.Filled.Chat, contentDescription = "Chat", tint = Color.White, modifier = Modifier.size(26.dp))
                }
                if (totalMessages > 0) {
                    Box(
                        modifier = Modifier.size(16.dp).background(Color.Red, CircleShape).align(Alignment.TopEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$totalMessages", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun IndividualOrderCard(
    order: OrderUIModel,
    status: OrderStatus,
    actionText: String,
    onActionClick: (String, OrderStatus) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Mã đơn và Trạng thái
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Mã đơn: #${order.id.takeLast(6).uppercase()}", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Text(status.label, color = TealGreen, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)

            // Thông tin sản phẩm
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = order.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF5F5F5)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(order.productName, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1)
                    Text(order.authorName, fontSize = 13.sp, color = GrayText)
                    Text("Số lượng: ${order.quantity}", fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer: Tổng tiền và Nút hành động
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Tổng thanh toán", fontSize = 11.sp, color = GrayText)
                    Text(order.totalPrice.toCurrencyString(), fontWeight = FontWeight.Bold, color = SuccessPriceColor, fontSize = 17.sp)
                }

                if (actionText.isNotEmpty()) {
                    Button(
                        onClick = { onActionClick(order.id, status) },
                        colors = ButtonDefaults.buttonColors(
                            // Màu xám cho nút Hủy, màu hồng cho các nút còn lại
                            containerColor = if (status == OrderStatus.PENDING) Color(0xFFE0E0E0) else PinkBtn,
                            contentColor = if (status == OrderStatus.PENDING) Color.DarkGray else Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(actionText, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
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
    onMessageClick: () -> Unit,
    onActionClick: (String, OrderStatus) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8))) {
        OrdersHeader(totalMessages, onBackClick, onMessageClick)

        // Thanh Tab cuộn được nếu quá dài
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = Color.White,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = HeaderPurple
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if(selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        val currentSection = orderSections.getOrNull(selectedTabIndex)

        LazyColumn(
            modifier = Modifier.fillMaxSize().navigationBarsPadding(),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 120.dp), // Padding bottom lớn né menu
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                item {
                    Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = HeaderPurple)
                    }
                }
            } else if (errorMessage != null) {
                item {
                    Text("Lỗi: $errorMessage", color = Color.Red, textAlign = TextAlign.Center, modifier = Modifier.padding(24.dp))
                }
            } else if (currentSection == null || currentSection.orders.isEmpty()) {
                item {
                    Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Không có đơn hàng nào trong mục này.", color = GrayText)
                    }
                }
            } else {
                // Hiển thị danh sách các Card đơn lẻ
                items(currentSection.orders.size) { index ->
                    IndividualOrderCard(
                        order = currentSection.orders[index],
                        status = currentSection.status,
                        actionText = currentSection.actionText,
                        onActionClick = onActionClick
                    )
                }
            }
        }
    }
}