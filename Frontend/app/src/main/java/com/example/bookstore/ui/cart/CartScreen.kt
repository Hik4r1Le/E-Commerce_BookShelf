import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale
import com.example.bookstore.model.CartItemData


// ---- MÀU SẮC ----
private val CustomPrimary = Color(0xFFAAB7E1)
private val CustomBackground = Color(0xFFF3F3F5)
private val CustomCardBackground = Color(0xFFF5D3C4)
private val CustomButtonColor = Color(0xFFE6A9BD)
private val CustomQuantityButton = Color(0xFFEFEFEF)
private val CustomOnPrimary = Color(0xFF333333)

// ---- GIAO DIỆN CHỦ ĐỀ ----
@Composable
private fun CartAppTheme(content: @Composable () -> Unit) {
    val CustomColorScheme = MaterialTheme.colorScheme.copy(
        primary = CustomPrimary,
        background = CustomBackground,
        surface = CustomCardBackground,
        onSurface = Color.Black,
        onPrimary = CustomOnPrimary
    )

    MaterialTheme(
        colorScheme = CustomColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}

// ---- MÀN HÌNH GIỎ HÀNG ----
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartScreen(
    onNavigateBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val cartItems = remember {
        mutableStateListOf(
            CartItemData(1, "Muôn kiếp nhân sinh", "Nguyễn Phong", 99000.0, R.drawable.muon_kiep, "url_fake_1", 2),
            CartItemData(2, "Cho tôi xin một vé đi tuổi thơ", "Nguyễn Nhật Ánh", 69000.0, R.drawable.tuoi_tho, "url_fake_2", 3),
            CartItemData(3, "Nhà giả kim", "Paulo Coelho", 89000.0, R.drawable.nha_gia_kim, "url_fake_3", 1),
            CartItemData(4, "Tuổi trẻ đáng giá bao nhiêu?", "Rosie Nguyễn", 72000.0, R.drawable.tuoi_tre, "url_fake_4", 2)
        )
    }

    val currentTotal by remember(cartItems) {
        derivedStateOf {
            cartItems.sumOf { if (it.isChecked) it.price * it.quantity else 0.0 }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Giỏ hàng", fontWeight = FontWeight.Bold, color = CustomOnPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại", tint = CustomOnPrimary)
                    }
                },
                actions = {
                    Box(modifier = Modifier.padding(end = 16.dp)) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = CustomOnPrimary)
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .offset(x = 10.dp, y = (-5).dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Red)
                                .align(Alignment.TopEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("9", color = Color.White, fontSize = 10.sp)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CustomPrimary)
            )
        },
        bottomBar = { CartSummary(currentTotal, onCheckout) }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            EmptyCartMessage(Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(CustomBackground),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cartItems, key = { it.id }) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CustomBackground),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.isChecked,
                            onCheckedChange = { isChecked ->
                                val index = cartItems.indexOfFirst { it.id == item.id }
                                if (index != -1)
                                    cartItems[index] = cartItems[index].copy(isChecked = isChecked)
                            },
                            modifier = Modifier.size(36.dp)
                        )

                        CartItemContent(
                            item = item,
                            onQuantityChange = { newQty ->
                                val index = cartItems.indexOfFirst { it.id == item.id }
                                if (index != -1)
                                    cartItems[index] = cartItems[index].copy(quantity = newQty)
                            },
                            onRemove = { cartItems.remove(item) }
                        )
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

// ---- HÀM ĐỊNH DẠNG GIÁ TIỀN ----
private fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return format.format(amount).replace("₫", "đ").trim()
}

// ---- ITEM TRONG GIỎ ----
@Composable
private fun CartItemContent(
    item: CartItemData,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .padding(start = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CustomCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.resId),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 80.dp, height = 110.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Filled.Delete, contentDescription = null, tint = Color.Gray)
                    }
                }

                Text(item.author, fontSize = 12.sp, color = Color.Gray.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatCurrency(item.price),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(12.dp))

                QuantityCounter(
                    quantity = item.quantity,
                    onDecrease = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) },
                    onIncrease = { onQuantityChange(item.quantity + 1) }
                )
            }
        }
    }
}

// ---- BỘ ĐẾM SỐ LƯỢNG ----
@Composable
private fun QuantityCounter(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CounterButton("-", onClick = onDecrease, isStart = true)
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(28.dp)
                .background(CustomQuantityButton)
                .border(1.dp, Color(0xFFC0C0C0)),
            contentAlignment = Alignment.Center
        ) {
            Text(quantity.toString(), fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
        CounterButton("+", onClick = onIncrease, isEnd = true)
    }
}

@Composable
private fun CounterButton(label: String, onClick: () -> Unit, isStart: Boolean = false, isEnd: Boolean = false) {
    val shape = when {
        isStart -> RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
        isEnd -> RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
        else -> RoundedCornerShape(0.dp)
    }

    Box(
        modifier = Modifier
            .size(width = 28.dp, height = 28.dp)
            .background(CustomQuantityButton, shape)
            .border(1.dp, Color(0xFFC0C0C0), shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 16.sp, color = Color.DarkGray)
    }
}

// ---- PHẦN TỔNG KẾT ----
@Composable
private fun CartSummary(total: Double, onCheckout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tổng cộng:", fontSize = 18.sp, color = Color.Black)
            Text(
                text = formatCurrency(total),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onCheckout,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CustomButtonColor)
        ) {
            Text("Mua hàng", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

// ---- GIỎ HÀNG TRỐNG ----
@Composable
private fun EmptyCartMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(CustomBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Giỏ hàng trống", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Hãy thêm sản phẩm vào giỏ hàng của bạn!", fontSize = 16.sp, color = Color.LightGray)
        }
    }
}
