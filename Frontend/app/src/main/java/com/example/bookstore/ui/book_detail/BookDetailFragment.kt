package com.example.bookstore.ui.book_detail

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import com.example.bookstore.model.BookDetailData
import com.example.bookstore.model.UserCommentData
import com.example.bookstore.ui.theme.BookstoreTheme
import androidx.compose.material3.Card
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign

class BookDetailFragment : Fragment(R.layout.fragment_book_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {

            }
        }
    }
}

@Composable
fun BookDetailScreen(
    book: BookDetailData,
    comments: List<UserCommentData>,
    onBackClick: () -> Unit,
    onAddToCart: (BookDetailData) -> Unit
) {
    var quantity = remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        // Header (top bar)
        BookDetailHeader(onBackClick = onBackClick)

        // Main scrollable content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            item {
                BookDetailCard(
                    book = book,
                    onAddToCart = onAddToCart
                )
            }

            item {
                BookDescriptionCard(description = book.description)
            }

            // Reader Comments
            item {
                Text(
                    text = "Bình luận của người đọc",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(comments) { comment ->
                UserCommentCard(
                    username = comment.username,
                    rating = comment.rating,
                    comment = comment.comment
                )
            }

            item { Spacer(modifier = Modifier.height(80.dp)) } // space for footer overlap
        }

        // Footer (your version)
        FooterSection(
            quantity = quantity.value,
            onIncrease = { quantity.value++ },
            onDecrease = { if (quantity.value > 1) quantity.value-- },
            price = book.price * quantity.value,
            onAddToCart = { onAddToCart(book) }
        )
    }
}

@Composable
fun BookDetailHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFFB0AEE0))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.arrow_back_icon),
                contentDescription = "Back",
                tint = Color.White,

            )
        }
        Icon(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = null,
            tint = Color(0xFFFFB300),
            modifier = Modifier
                //.align(Alignment.TopEnd)
                .size(48.dp)
                .offset(x = (-12).dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_shopping_cart_24dp),
            contentDescription = null,
            tint = Color(0xFFFFFFFF),
            modifier = Modifier
                //.align(Alignment.TopEnd)
                .size(24.dp)
                .offset(x = (-16).dp)
        )
    }
}

@Composable
fun BookDetailCard(
    book: BookDetailData,
    onAddToCart: (BookDetailData) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5D3C4)), // Peach tone
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = book.category,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Image(
                painter = painterResource(id = book.imageRes),
                contentDescription = book.title,
                modifier = Modifier
//                    .height(180.dp)
//                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // page indicator (PLACE HOLDER)
            Text(
                text = "1/4",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = book.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Tác giả: ${book.author}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    val starValue = book.star - index
                    DisplayStar(starValue)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BookDescriptionCard(description: String) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5D3C4)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Mô tả sản phẩm",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = description,
                textAlign = TextAlign.Justify,
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun FooterSection(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    price: Double,
    onAddToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "SL",
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )

                IconButton(
                    onClick = onDecrease,
//                    modifier = Modifier
//                        .size(20.dp)
//                        .background(Color.LightGray.copy(alpha = 0.5f), CircleShape)
                ) {
                    Text("—",
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 2.dp, vertical = 2.dp))
                }

                Text(
                    text = quantity.toString(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )

                IconButton(
                    onClick = onIncrease,
//                    modifier = Modifier
//                        .size(20.dp)
//                        .background(Color.LightGray.copy(alpha = 0.5f), CircleShape)
                ) {
                    Text("+",
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 2.dp, vertical = 2.dp))
                }
            }

            Text(
                text = String.format("%,.0f đ", price),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )
        }

        Button(
            onClick = onAddToCart,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5ADBC)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Thêm vào giỏ hàng",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun DisplayStar(star: Float) {
    when {
        star >= 1f ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color(0xFFFF9800)
            )
        star >= 0.5f ->
            Icon(imageVector = Icons.Filled.StarHalf,
                contentDescription = null,
                tint = Color(0xFFFF9800)
            )
        else ->
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = null,
                tint = Color(0xFFFF9800)
            )
    }
}

@Composable
fun UserCommentCard(
    username: String,
    rating: Float,
    comment: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5D3C4)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Row {
                    repeat(5) { index ->
                        val starValue = rating - index
                        Icon(
                            imageVector = when {
                                starValue >= 1f -> Icons.Filled.Star
                                starValue >= 0.5f -> Icons.Filled.StarHalf
                                else -> Icons.Filled.StarBorder
                            },
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview
@Composable
fun PreViewHeader(){
    BookDetailHeader(
        onBackClick = {},
    )
}

@Preview
@Composable
fun PreviewBookDetail(){
    val data = BookDetailData(
        id = 1,
        title = "Cánh đồng bất tận",
        author = "Nguyễn Ngọc Tự",
        category = "Văn học",
        imageRes = R.drawable.book5,
        description = "",
        price = 1.0,
        star = 5f
    )
    BookDetailCard(
        onAddToCart = {},
        book = data
    )
}

@Preview
@Composable
fun PreviewBookDes(){
    val data = """
            Cánh Đồng Bất Tận đưa người đọc vào đời sống giản dị, đầy chất thơ của miền Tây sông nước, khắc họa cô đơn, tình yêu và hy vọng qua những mảnh đời mộc mạc.

            Phù hợp với: Người yêu văn học miền Tây, thích những câu chuyện tinh tế, giàu nhân văn.
            """.trimIndent()
     BookDescriptionCard(
        data
    )
}

@Preview (
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PreviewFooter(){
    FooterSection(
        quantity = 2,
        onIncrease = {},
        onDecrease = {},
        price = 86000.0,
        onAddToCart = {}
    )
}

@Preview
@Composable
fun PreviewComment() {
    UserCommentCard(
        username = "hik4r1le",
        rating = 4.5f,
        comment = "Sách đỉnh vcl",
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PreviewBookDetailScreen() {
    val sampleBook = BookDetailData(
        id = 1,
        title = "Cánh đồng bất tận",
        author = "Nguyễn Ngọc Tư",
        category = "Văn học",
        imageRes = R.drawable.book5,
        description = """
            Cánh Đồng Bất Tận đưa người đọc vào đời sống giản dị, đầy chất thơ của miền Tây sông nước, 
            khắc họa cô đơn, tình yêu và hy vọng qua những mảnh đời mộc mạc.

            Phù hợp với: Người yêu văn học miền Tây, thích những câu chuyện tinh tế, giàu nhân văn.
        """.trimIndent(),
        price = 86000.0,
        star = 4.5f
    )

    val sampleComments = listOf(
        UserCommentData("hik4r1le", 5f, "Sách đỉnh vcl"),
        UserCommentData("BaoTram161", 3.5f, "Ok"),
        UserCommentData("ngquocvuong23", 2f, "vl cai deo gi day")
    )

    BookstoreTheme {
        BookDetailScreen(
            book = sampleBook,
            comments = sampleComments,
            onBackClick = {},
            onAddToCart = {}
        )
    }
}