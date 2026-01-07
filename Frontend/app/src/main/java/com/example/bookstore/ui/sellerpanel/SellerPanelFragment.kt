package com.example.bookstore.ui.sellerpanel

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.example.bookstore.R
import com.example.bookstore.model.sellerpanel.*

// Color

private val PurpleTop = Color(0xFFA7AAE1)
private val YellowIcon = Color(0xFFFFA629)
private val BeigeCard = Color(0xFFFFF3EC)
private val TabGreen = Color(0xFF12793D)
private val PinkButton = Color(0xFFF2AEBB)

// Fragment
class SellerPanelFragment : Fragment(R.layout.fragment_seller_panel) {
    private val viewModel: SellerPanelViewModel by viewModels {
        SellerPanelViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData()

        view.findViewById<ComposeView>(R.id.composeView).setContent {
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current

            // Trạng thái điều khiển Dialog
            var showDialog by remember { mutableStateOf(false) }
            var editingProduct by remember { mutableStateOf<SellerProductUIModel?>(null) }

            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F5F5)) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column {
                            SellerTopBar(onBack = { findNavController().navigateUp() })
                            SellerTabBar(uiState.selectedTab, viewModel::onTabSelected)

                            if (uiState.selectedTab == 0) {
                                ProductSection(
                                    products = uiState.products,
                                    onAddClick = {
                                        editingProduct = null // Reset để hiểu là thêm mới
                                        showDialog = true
                                    },
                                    onEdit = { product ->
                                        editingProduct = product // Gán sản phẩm để hiểu là đang sửa
                                        showDialog = true
                                    },
                                    onDelete = viewModel::deleteProduct
                                )
                            } else {
                                OrderSection(uiState.orders, viewModel::updateOrderStatus)
                            }
                        }

                        if (showDialog) {
                            UpsertProductDialog(
                                categories = uiState.categories,
                                initialProduct = editingProduct, // Truyền dữ liệu cũ nếu là Edit
                                onDismiss = { showDialog = false },
                                onConfirm = { name, author, desc, price, qty, catId, uri ->
                                    if (editingProduct == null) {
                                        viewModel.addProduct(context, name, author, desc, price, qty, catId, uri)
                                    } else {
                                        viewModel.updateProduct(context, editingProduct!!.id, name, author, desc, price, qty, catId, uri)
                                    }
                                    showDialog = false
                                }
                            )
                        }

                        if (uiState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PurpleTop)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SellerTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(PurpleTop).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White) }
        Text("SELLER PANEL", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.ChatBubbleOutline, null, tint = Color.White)
    }
}

@Composable
fun SellerTabBar(selected: Int, onSelect: (Int) -> Unit) {
    TabRow(selectedTabIndex = selected, containerColor = Color.White, contentColor = TabGreen) {
        Tab(selected = selected == 0, onClick = { onSelect(0) }) {
            Text("Sản phẩm của tôi", modifier = Modifier.padding(16.dp), fontWeight = if(selected==0) FontWeight.Bold else FontWeight.Normal)
        }
        Tab(selected = selected == 1, onClick = { onSelect(1) }) {
            Text("Đơn hàng", modifier = Modifier.padding(16.dp), fontWeight = if(selected==1) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Composable
fun ProductSection(
    products: List<SellerProductUIModel>,
    onAddClick: () -> Unit, // Đã sử dụng lại
    onEdit: (SellerProductUIModel) -> Unit,
    onDelete: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        // Đưa SectionHeader quay trở lại
        SectionHeader(title = "Sản phẩm của tôi", onAddClick = onAddClick)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(products) { product ->
                ProductCard(product, onEdit = { onEdit(product) }, onDelete = onDelete)
            }
            item { Spacer(modifier = Modifier.height(100.dp)) }
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
fun ProductCard(
    product: SellerProductUIModel,
    onEdit: () -> Unit,
    onDelete: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(BeigeCard)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(90.dp, 120.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(product.name, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(product.authorName, fontSize = 13.sp, color = Color.Gray)
                Text("Tồn kho: ${product.quantity}", fontSize = 13.sp)
                Text("${product.price.toInt()}đ", color = Color.Red, fontWeight = FontWeight.Bold)

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    // Nút SỬA: Đã kích hoạt
                    Button(
                        onClick = onEdit,
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) { Text("Sửa", fontSize = 12.sp) }

                    Spacer(Modifier.width(8.dp))

                    // Nút XÓA
                    Button(
                        onClick = { onDelete(product.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) { Text("Xóa", fontSize = 12.sp) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertProductDialog(
    categories: List<SellerCategoryItem>, // Danh sách lấy từ API GET (phần data.category)
    initialProduct: SellerProductUIModel? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String, Uri?) -> Unit
) {
    // Khởi tạo State: Nếu là Sửa (initialProduct != null) thì điền sẵn, nếu Thêm thì để trống
    var name by remember { mutableStateOf(initialProduct?.name ?: "") }
    var author by remember { mutableStateOf(initialProduct?.authorName ?: "") }
    var desc by remember { mutableStateOf(initialProduct?.description ?: "") } // Đã lấy được description
    var price by remember { mutableStateOf(initialProduct?.price?.toInt()?.toString() ?: "") }
    var qty by remember { mutableStateOf(initialProduct?.quantity?.toString() ?: "") }

    // Lấy Category ID hiện tại của sản phẩm để làm mặc định cho Dropdown
    var selectedCatId by remember { mutableStateOf(initialProduct?.productCategoryId ?: "") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri = it }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialProduct == null) "Thêm sản phẩm mới" else "Cập nhật sản phẩm", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(name, { name = it }, label = { Text("Tên sách") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(author, { author = it }, label = { Text("Tác giả") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))

                // Trường Mô tả: Hiển thị dữ liệu khi Cập nhật
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Mô tả sản phẩm") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    maxLines = 5
                )

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(price, { price = it }, label = { Text("Giá (VNĐ)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(qty, { qty = it }, label = { Text("Số lượng kho") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(16.dp))
                Text("Thể loại sách", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)

                // --- Dropdown Category ---
                var expanded by remember { mutableStateOf(false) }
                // Tìm tên category tương ứng với ID đang chọn để hiển thị lên nhãn
                val selectedCatName = categories.find { it.id == selectedCatId }?.name ?: "Chọn thể loại"

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCatName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name) },
                                onClick = {
                                    selectedCatId = cat.id // Lưu lại ID để gửi API
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Preview ảnh
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp).clip(RoundedCornerShape(8.dp)).align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                }

                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if(imageUri != null) TabGreen else PurpleTop)
                ) {
                    Text(if(imageUri == null) "Chọn ảnh sản phẩm" else "Đã chọn ảnh mới")
                }
            }
        },
        confirmButton = {
            Button(
                // Khi nhấn Xác nhận, toàn bộ thông tin (bao gồm selectedCatId) sẽ được gửi đi
                onClick = { onConfirm(name, author, desc, price, qty, selectedCatId, imageUri) },
                enabled = name.isNotBlank() && price.isNotBlank() && selectedCatId.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = PinkButton)
            ) {
                Text("XÁC NHẬN", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("HỦY", color = Color.Gray) }
        }
    )
}

@Composable
fun OrderSection(orders: List<SellerOrderUIModel>, onUpdate: (String, String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        items(orders) { order ->
            OrderCard(order, onUpdate)
        }
        item { Spacer(modifier = Modifier.height(100.dp)) }
    }
}

@Composable
fun OrderCard(order: SellerOrderUIModel, onUpdate: (String, String) -> Unit) {
    val nextLabel = when(order.status) {
        "PENDING" -> "Xác nhận đơn"
        "PROCESSING" -> "Bắt đầu giao"
        else -> null
    }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), colors = CardDefaults.cardColors(Color.White), border = BorderStroke(1.dp, Color.LightGray)) {
        Column(Modifier.padding(16.dp)) {
            Text("Mã đơn: #${order.orderId.takeLast(8)}", fontWeight = FontWeight.Bold)
            Text("Khách: ${order.customerName}", fontSize = 13.sp)
            Text("Địa chỉ: ${order.fullAddress}", fontSize = 12.sp, color = Color.Gray)
            Divider(Modifier.padding(vertical = 8.dp))
            Row {
                AsyncImage(model = order.imageUrl, contentDescription = null, modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp)))
                Column(Modifier.padding(start = 12.dp)) {
                    Text(order.productName, fontWeight = FontWeight.Medium)
                    Text("Số lượng: ${order.quantity}", fontSize = 12.sp)
                }
            }
            Text("Tổng: ${order.totalPrice}đ", color = Color.Red, modifier = Modifier.align(Alignment.End), fontWeight = FontWeight.Bold)

            if (nextLabel != null) {
                // Hiển thị nút bấm nếu Seller còn quyền chuyển trạng thái
                Button(
                    onClick = { onUpdate(order.orderId, order.status) },
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PinkButton)
                ) {
                    Text(nextLabel, color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else {
                // Nếu đang giao hoặc đã giao, chỉ hiện text thông báo
                val statusColor = if (order.status == "DELIVERED") TabGreen else Color.Blue
                val statusText = when(order.status) {
                    "SHIPPING" -> "Đang giao hàng..."
                    "DELIVERED" -> "Giao hàng thành công"
                    "CANCELLED" -> "Đơn đã hủy"
                    else -> order.status
                }
                Text(
                    text = statusText,
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
