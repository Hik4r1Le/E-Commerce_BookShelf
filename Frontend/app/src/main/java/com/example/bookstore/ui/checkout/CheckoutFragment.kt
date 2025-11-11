package com.example.bookstore.ui.checkout

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.bookstore.R
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import com.example.bookstore.ui.theme.BookstoreTheme
import com.example.bookstore.model.CheckoutBook
import com.example.bookstore.model.DeliveryOption
import java.text.NumberFormat
import java.util.Locale

class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        val testBooks = listOf(
            CheckoutBook(R.drawable.book1, "Sách", "Lập trình Kotlin", "John Doe", "150000", 2),
            CheckoutBook(R.drawable.book2, "Truyện", "Harry Potter", "J.K. Rowling", "120000", 1)
        )
        composeView.setContent {
            BookstoreTheme {
                CheckoutScreen(
                    books = testBooks,
                    onBackClick = { /* pop */ },
                    onOrder = { /* place order */ }
                )
            }
        }
    }
}

@Composable
fun CheckoutScreen(
    books: List<CheckoutBook>,
    onBackClick: () -> Unit,
    onOrder: () -> Unit
    ) {
    var selectedDelivery by remember { mutableStateOf(DeliveryOption.TIET_KIEM) }

    Column(modifier = Modifier.fillMaxSize()) {
        Header(onBackClick)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item { AddressInput() }
            item { OrderList(books) }
            item { Delivery(selected = selectedDelivery, onSelect = { selectedDelivery = it }) }
        }

        Footer(books = books, selectedDelivery = selectedDelivery, onOrder = onOrder)
    }
}

@Composable
fun Header(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFB0AEE0),
            )
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
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
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
            onValueChange = { address2 = it },
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
fun OrderCard(
    orderBook: CheckoutBook,
    modifier: Modifier = Modifier
    ) {
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
            Image(
                painter = painterResource(orderBook.imageID),
                contentDescription = null,
                modifier = Modifier
                    .width(70.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = orderBook.category,
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Text(
                    text = orderBook.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 2.dp),
                    maxLines = 1
                )

                Text(
                    text = orderBook.author,
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Số lượng: ${orderBook.quantity}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFD9D9D9), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = orderBook.price,
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
fun OrderList(
    orderBooks: List<CheckoutBook>,
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(
            text = "Sản phẩm",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            orderBooks.forEach { book ->
                OrderCard(orderBook = book)
            }
        }
    }
}


@Composable
fun Delivery(selected: DeliveryOption,
             onSelect: (DeliveryOption) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hình thức giao hàng",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

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
                        selectedColor = Color.Red,
                        unselectedColor = Color.LightGray
                    )
                )
                Spacer(Modifier.width(12.dp))
                Text(option.title, fontSize = 16.sp)
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${option.price}đ – ${option.time}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Composable
fun Footer(
    books: List<CheckoutBook>,
    selectedDelivery: DeliveryOption,
    onOrder: () -> Unit
) {
    val total = calculateTotal(books, selectedDelivery.price)
    val formattedTotal = NumberFormat.getInstance(Locale("vi", "VN")).format(total)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            "Tổng:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "${formattedTotal}đ",
                color = Color.Green,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Spacer(Modifier.weight(0.5f))
            Button(
                onClick = onOrder,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5ADBC)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text(
                    "Đặt hàng",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            }
        }
    }
}

fun calculateTotal(books: List<CheckoutBook>, deliveryPrice: Int): Int {
    val booksTotal = books.sumOf { it.price.toIntOrNull() ?: 0 * it.quantity }
    return booksTotal + deliveryPrice
}

@Preview
@Composable
fun HeaderPreview() {
    BookstoreTheme {
        Header { }
    }
}
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AddressPreview() {
    BookstoreTheme {
        AddressInput()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun OrderBookCardPreview() {
    val sample = CheckoutBook(
        imageID = R.drawable.book1, // replace with your drawable
        category = "Sách Truyền cảm hứng",
        title = "Muôn kiếp nhân sinh",
        author = "Nguyễn Phong",
        price = "99.000đ",
        quantity = 1
    )

    BookstoreTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            OrderCard(
                orderBook = sample,
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun OrderBookListPreview() {
    val sampleList = listOf(
        CheckoutBook(
            imageID = R.drawable.book1,
            category = "Sách Truyền cảm hứng",
            title = "Muôn kiếp nhân sinh",
            author = "Nguyễn Phong",
            price = "99.000đ",
            quantity = 1
        ),
        CheckoutBook(
            imageID = R.drawable.book2,
            category = "Sách Thiếu nhi",
            title = "Cho tôi xin một vé đi tuổi thơ",
            author = "Nguyễn Nhật Ánh",
            price = "69.000đ",
            quantity = 2
        )
    )

    BookstoreTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            OrderList(
                orderBooks = sampleList,
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun DeliveryPreview() {
    BookstoreTheme {
        var selected by remember { mutableStateOf(DeliveryOption.TIET_KIEM) }
        Delivery(selected = selected, onSelect = { selected = it })
    }
}

@Preview(showBackground = true)
@Composable
fun FooterPreview() {
    val sampleBooks = listOf(
        CheckoutBook(0,
            "Sách",
            "Lập trình Kotlin",
            "John Doe",
            "120000",
            2)
    )
    BookstoreTheme {
        Footer(
            books = sampleBooks,
            selectedDelivery = DeliveryOption.TIET_KIEM,
            onOrder = {}
        )
    }
}

@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun CheckoutScreenPreview() {
    val testBooks = listOf(
        CheckoutBook(R.drawable.book4, "Sách", "Lập trình Kotlin", "John Doe", "150000", 2),
        CheckoutBook(R.drawable.book3, "Truyện", "Harry Potter", "J.K. Rowling", "120000", 1)
    )

    BookstoreTheme {
        CheckoutScreen(
            books = testBooks,
            onBackClick = {},
            onOrder = {}
        )
    }
}