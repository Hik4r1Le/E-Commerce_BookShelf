package com.example.bookstore.ui.sellerpanel

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.R
import com.example.bookstore.model.SellerProduct
import com.example.bookstore.model.Order
import com.example.bookstore.model.OrderProduct

// Color

private val PurpleTop = Color(0xFFA7AAE1)
private val YellowIcon = Color(0xFFFFA629)
private val BeigeCard = Color(0xFFFFF3EC)
private val TabGreen = Color(0xFF12793D)
private val PinkButton = Color(0xFFF2AEBB)

// Fragment

class SellerPanelFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SellerPanelScreen()
            }
        }
    }
}

// Main

@Composable
fun SellerPanelScreen(
    viewModel: SellerPanelViewModel = viewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        SellerTopBar()
        SellerTabBar(viewModel.selectedTab) {
            viewModel.selectedTab = it
        }

        if (viewModel.selectedTab == 0) {
            ProductSection(viewModel.products) {
                showAddDialog = true
            }
        } else {
            OrderSection(
                orders = viewModel.orders,
                onUpdateStatus = { id, status ->
                    viewModel.updateOrderStatus(id, status)
                }
            )
        }
    }

    if (showAddDialog) {
        AddProductDialog(
            onDismiss = { showAddDialog = false },
            onAdd = {
                viewModel.addProduct(it)
                showAddDialog = false
            }
        )
    }
}

// Top Bar

@Composable
fun SellerTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PurpleTop)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
        Spacer(Modifier.weight(1f))
        BadgedBox(badge = { Badge { Text("20") } }) {
            Icon(Icons.Default.ChatBubbleOutline, null, tint = Color.White)
        }
    }
}

// Tab Bar

@Composable
fun SellerTabBar(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TabItem("Sản phẩm của tôi", selected == 0) { onSelect(0) }
        TabItem("Đơn hàng", selected == 1) { onSelect(1) }
    }
}

@Composable
fun TabItem(title: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = TabGreen
        )
    }
}

// Product

@Composable
fun ProductSection(
    products: List<SellerProduct>,
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        SectionHeader("Sản phẩm của tôi", onAddClick)
        LazyColumn {
            items(products.size) { index ->
                ProductCard(products[index])
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.Search, null, tint = YellowIcon)
        Spacer(Modifier.width(12.dp))
        Icon(
            Icons.Default.Add,
            null,
            tint = YellowIcon,
            modifier = Modifier.clickable { onAddClick() }
        )
    }
}

@Composable
fun ProductCard(product: SellerProduct) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(BeigeCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                //painter = painterResource(product.imageRes),
                painter = rememberAsyncImagePainter(
                    model = product.imageUri ?: R.drawable.book6
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(90.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(product.title, fontWeight = FontWeight.Bold)
                Text(product.author, fontSize = 13.sp)
                Text("Trạng thái: ${product.status}", fontSize = 13.sp)
                Text(product.price, color = Color.Red, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row {
                    SmallGrayButton("Sửa")
                    Spacer(Modifier.width(6.dp))
                    SmallGrayButton("SL: ${product.quantity}")
                    Spacer(Modifier.width(6.dp))
                    SmallGrayButton("Xóa")
                }
            }
        }
    }
}

// Add Product

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onAdd: (SellerProduct) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageRes by remember { mutableStateOf(R.drawable.book6) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

//    val launcher = rememberLauncherForActivityResult(
//        ActivityResultContracts.GetContent()
//    ) { imageRes = R.drawable.book6 }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onAdd(
                    SellerProduct(
                        title = title,
                        author = author,
                        price = price,
                        status = "Còn hàng",
                        quantity = 1,
                        imageUri = imageUri
                    )
                )
            }) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        },
        title = { Text("Thêm sản phẩm") },
        text = {
            Column {
                OutlinedTextField(title, { title = it }, label = { Text("Tên sách") })
                OutlinedTextField(author, { author = it }, label = { Text("Tác giả") })
                OutlinedTextField(price, { price = it }, label = { Text("Giá") })
                Spacer(Modifier.height(8.dp))
                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Chọn ảnh")
                }
            }
        }
    )
}

// Order

@Composable
fun OrderSection(
    orders: List<Order>,
    onUpdateStatus: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Đơn hàng", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.Search, null, tint = YellowIcon)
        }

        LazyColumn {
            items(orders.size) { index ->
                val buttonText =
                    if (orders[index].id == "002") "Xác nhận" else "Đã giao"
                OrderCard(
                    order = orders[index],
                    buttonText = buttonText,
                    onUpdateStatus = onUpdateStatus
                )
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    buttonText: String,
    onUpdateStatus: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(BeigeCard)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row {
                Text("Mã đơn: #${order.id} ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("Khách hàng: ${order.customer}  Ngày: ${order.date}", fontSize = 12.sp)
            }
            Spacer(Modifier.height(6.dp))
            Row {
                Text("Trạng thái: ${order.status}", fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                Text("Tổng tiền: ${order.total}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))

            order.products.forEach {
                OrderProductItem(it.title, it.quantity, it.price, it.imageRes)
                Spacer(Modifier.height(6.dp))
            }

            Text("Địa chỉ: ${order.address}", fontSize = 12.sp)
            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .background(PinkButton, RoundedCornerShape(16.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(buttonText, fontSize = 12.sp, color = Color.White)
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("Mới", "Đang xử lý", "Xác nhận").forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status) },
                        onClick = {
                            onUpdateStatus(order.id, status)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderProductItem(
    title: String,
    quantity: Int,
    price: String,
    imageRes: Int
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("Số lượng: $quantity", fontSize = 12.sp)
            Text(price, fontSize = 12.sp, color = Color.Red)
        }
    }
}

// Common

@Composable
fun SmallGrayButton(text: String) {
    Box(
        modifier = Modifier
            .background(Color.LightGray, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 12.sp)
    }
}

// Previews

@Preview(showBackground = true)
@Composable
fun Preview_Header_TopBar() {
    Column {
        SellerTopBar()
        SellerTabBar(selected = -1) {}
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_Product_Tab() {
    Column {
        SellerTabBar(selected = 0) {}
        ProductSection(
            listOf(
                SellerProduct("Muôn kiếp nhân sinh", "Nguyên Phong", "99.000đ", "Còn hàng", 50, null),
                SellerProduct("Cho tôi xin một vé đi tuổi thơ", "Nguyễn Nhật Ánh", "69.000đ", "Hết hàng", 0, null)
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_Header_Product_Tab() {
    Column {
        SellerTopBar()
        SellerTabBar(selected = 0) {}
        ProductSection(
            listOf(
                SellerProduct("Muôn kiếp nhân sinh", "Nguyên Phong", "99.000đ", "Còn hàng", 50, null),
                SellerProduct("Cho tôi xin một vé đi tuổi thơ", "Nguyễn Nhật Ánh", "69.000đ", "Hết hàng", 0, null)
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_Order_Without_Header() {
    Column {
        SellerTabBar(selected = 1) {}
        OrderSection(
            orders = listOf(
                Order(
                    "001",
                    "Nguyễn Văn A",
                    "12/12/2025",
                    "Mới",
                    "257.000đ",
                    "Khu phố 34, P. Linh Xuân, TP.Hồ Chí Minh",
                    listOf(
                        OrderProduct("Muôn kiếp nhân sinh", 1, "99.000đ", R.drawable.book6),
                        OrderProduct("Nhà giả kim", 2, "89.000đ", R.drawable.book8)
                    )
                ),
                Order(
                    "002",
                    "Trần Thị B",
                    "13/12/2025",
                    "Đang xử lý",
                    "480.000đ",
                    "Ktx khu B, P. Linh Trung, TP. Hồ Chí Minh",
                    listOf(
                        OrderProduct("Muôn kiếp nhân sinh", 2, "99.000đ", R.drawable.book6),
                        OrderProduct("Nhà giả kim", 3, "89.000đ", R.drawable.book8)
                    )
                )
            ),
            onUpdateStatus = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_Order_Tab() {
    Column {
        SellerTopBar()
        SellerTabBar(selected = 1) {}
        OrderSection(
            orders = listOf(
                Order(
                    "001",
                    "Nguyễn Văn A",
                    "12/12/2025",
                    "Mới",
                    "257.000đ",
                    "Khu phố 34, P. Linh Xuân, TP.Hồ Chí Minh",
                    listOf(
                        OrderProduct("Muôn kiếp nhân sinh", 1, "99.000đ", R.drawable.book6),
                        OrderProduct("Nhà giả kim", 2, "89.000đ", R.drawable.book8)
                    )
                ),
                Order(
                    "002",
                    "Trần Thị B",
                    "13/12/2025",
                    "Đang xử lý",
                    "480.000đ",
                    "Ktx khu B, P. Linh Trung, TP. Hồ Chí Minh",
                    listOf(
                        OrderProduct("Muôn kiếp nhân sinh", 2, "99.000đ", R.drawable.book6),
                        OrderProduct("Nhà giả kim", 3, "89.000đ", R.drawable.book8)
                    )
                )
            ),
            onUpdateStatus = { _, _ -> }
        )
    }
}