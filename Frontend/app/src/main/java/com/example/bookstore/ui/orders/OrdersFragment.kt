package com.example.bookstore.ui.orders

import android.os.Bundle
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bookstore.R

// ----------------- Colors -----------------
private val TealGreen = Color(0xFF17A590)
private val GrayText = Color(0xFF757575)
private val GrayArrow = Color(0xFFBDBDBD)

// Data models
data class BookOrder(
    val title: String,
    val author: String,
    val price: Int,
    val quantity: Int,
    val imageRes: Int
)

data class OrderSection(
    val status: String,
    val orders: List<BookOrder>,
    val totalPrice: Int,
    val actionText: String? = null
)

// Header
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
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Text(
                text = "ĐƠN MUA",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = onMessageClick) {
                    Icon(
                        imageVector = Icons.Filled.Chat,
                        contentDescription = "Chat",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                if (totalMessages > 0) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(Color.Red, shape = CircleShape)
                            .align(Alignment.TopEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "$totalMessages",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// Section card
@Composable
fun SectionOrderCardFloat(section: OrderSection) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = section.status,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            section.orders.forEach { order ->
                OrderItem(order)
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            val isPickupOrDelivery = section.status in listOf("Chờ lấy hàng", "Chờ giao hàng")
            val label = if (section.status == "Chờ lấy hàng") "Tổng thanh toán" else "Thành tiền"

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${section.orders.sumOf { it.quantity }} sản phẩm", color = GrayText)
                Text("$label:", color = Color.Black)
                Text(
                    "${section.totalPrice}.000đ",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2AD549)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (section.actionText != null) {
                when (section.status) {
                    "Chờ giao hàng" -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Đơn hàng sẽ sớm được giao, vui lòng chú ý điện thoại",
                                fontSize = 12.sp,
                                color = TealGreen,
                                modifier = Modifier.weight(1f)
                            )
                            Text(">", color = GrayArrow, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Vui lòng chỉ nhấn “Đã nhận hàng” khi đơn hàng đã được giao đến bạn.",
                                fontSize = 12.sp,
                                color = GrayText,
                                modifier = Modifier.weight(1f)
                            )

                            Box(
                                modifier = Modifier
                                    .shadow(4.dp, RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF2AEBB), RoundedCornerShape(8.dp))
                                    .clickable {}
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(section.actionText, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    "Đã giao" -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Giao hàng thành công",
                                fontSize = 12.sp,
                                color = TealGreen,
                                modifier = Modifier.weight(1f)
                            )
                            Text(">", color = GrayArrow, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .shadow(4.dp, RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF2AEBB), RoundedCornerShape(8.dp))
                                    .clickable {}
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(section.actionText, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    else -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isPickupOrDelivery) {
                                Text(
                                    "Đơn hàng sẽ được chuẩn bị và chuyển đi trước 30-10-2025.",
                                    fontSize = 12.sp,
                                    color = TealGreen,
                                    modifier = Modifier.weight(1f)
                                )
                            } else Spacer(modifier = Modifier.weight(1f))

                            Box(
                                modifier = Modifier
                                    .shadow(4.dp, RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF2AEBB), RoundedCornerShape(8.dp))
                                    .clickable {}
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(section.actionText, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Single order item
@Composable
fun OrderItem(order: BookOrder) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFFF2AEBB), RoundedCornerShape(10.dp))
            .background(Color(0xFFF5D3C4), RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(order.imageRes),
            contentDescription = order.title,
            modifier = Modifier
                .size(70.dp)
                .background(Color.White, RoundedCornerShape(6.dp))
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(order.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(order.author, fontSize = 12.sp, color = Color.Gray)
            Text("Số lượng: ${order.quantity}", fontSize = 12.sp)
        }
        Text(
            "${order.price}.000đ",
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

private val sampleOrders = listOf(
    BookOrder("Muôn kiếp nhân sinh", "Nguyễn Phong", 99, 1, R.drawable.book6),
    BookOrder("Cho tôi xin một vé đi tuổi thơ", "Nguyễn Nhật Ánh", 69, 2, R.drawable.book7)
)

private val sampleSections = listOf(
    OrderSection("Chờ xác nhận", sampleOrders, 257, "Đang xử lý"),
    OrderSection("Chờ lấy hàng", sampleOrders, 257, "Liên hệ Shop"),
    OrderSection("Chờ giao hàng", sampleOrders, 257, "Đã nhận hàng"),
    OrderSection("Đã giao", sampleOrders, 257, "Mua lại")
)

// Fragment
class OrdersFragment : Fragment() {

    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            val selectedTabIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()
            val totalMessages by viewModel.totalMessages.collectAsStateWithLifecycle()
            val orderSections by viewModel.orderSections.collectAsStateWithLifecycle()

            OrdersContent(
                tabs = viewModel.tabs,
                selectedTabIndex = selectedTabIndex,
                totalMessages = totalMessages,
                orderSections = orderSections,
                onTabSelected = { viewModel.onTabSelected(it) },
                onBackClick = { requireActivity().onBackPressed() },
                onMessageClick = { viewModel.clearMessages() }
            )
        }
    }
}

// OrdersContent
@Composable
fun OrdersContent(
    tabs: List<String>,
    selectedTabIndex: Int,
    totalMessages: Int,
    orderSections: List<OrderSection>,
    onTabSelected: (Int) -> Unit,
    onBackClick: () -> Unit,
    onMessageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
    ) {
        OrdersHeader(totalMessages, onBackClick, onMessageClick)

        val tabTitles = tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        color = Color.Black,
                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (orderSections.isNotEmpty()) {
                item {
                    SectionOrderCardFloat(orderSections[selectedTabIndex])
                }
            }
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun PreviewSectionWaitingConfirmation() {
    SectionOrderCardFloat(sampleSections[0])
}

@Preview(showBackground = true)
@Composable
fun PreviewSectionWaitingPickup() {
    SectionOrderCardFloat(sampleSections[1])
}

@Preview(showBackground = true)
@Composable
fun PreviewSectionWaitingDelivery() {
    SectionOrderCardFloat(sampleSections[2])
}

@Preview(showBackground = true)
@Composable
fun PreviewSectionDelivered() {
    SectionOrderCardFloat(sampleSections[3])
}

@Preview(showBackground = true, heightDp = 520)
@Composable
fun PreviewOrdersScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }

    OrdersContent(
        tabs = listOf("Chờ xác nhận", "Chờ lấy hàng", "Chờ giao hàng", "Đã giao"),
        selectedTabIndex = selectedTabIndex,
        totalMessages = 8,
        orderSections = sampleSections,
        onTabSelected = { selectedTabIndex = it },
        onBackClick = {},
        onMessageClick = {}
    )
}
