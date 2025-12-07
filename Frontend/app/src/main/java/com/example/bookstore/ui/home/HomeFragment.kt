package com.example.bookstore.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.bookstore.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.example.bookstore.ui.theme.BookstoreTheme
import androidx.fragment.app.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.CircularProgressIndicator
import com.example.bookstore.model.products.HomeUIProduct
import coil.compose.AsyncImage
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {
                // Tự động gọi API khi Composable được tạo lần đầu
                LaunchedEffect(key1 = Unit) {
                    viewModel.loadHomeProducts()
                }

                HomeScreen(
                    viewModel = viewModel,
                    onBookClick = { productId ->
                        val action = HomeFragmentDirections.actionHomeToBookDetail(productId)
                        findNavController().navigate(action)
                    }
                )
            }
        }
    }
}

// HÀM EXTENSION ĐỊNH DẠNG TIỀN TỆ
fun Double.toCurrencyString(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(this).replace("₫", "VNĐ")
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onBookClick: (String) -> Unit
) {
    // Trích xuất trạng thái từ ViewModel
    val products = viewModel.products
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    // Phân loại sản phẩm (Ví dụ: Best Seller là những sản phẩm có tag "best_seller")
    val bestSellerProducts = products.filter { it.isBestSeller }
    val recommendedProducts = products.filter { !it.isBestSeller }

    Column(modifier = Modifier
        //.padding(16.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        HomeHeader(
            onProfileClick = {},
            onSearchClick = {},
            //modifier = Modifier.weight(1f)
        )
        // Hiển thị trạng thái tải (Loading)
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
                Text("Đang tải dữ liệu...", modifier = Modifier.padding(top = 16.dp))
            }
        } else if (errorMessage != null) {
            Text("Lỗi: $errorMessage", color = Color.Red, modifier = Modifier.padding(16.dp))
            // Có thể thêm nút Thử lại (Retry) ở đây
        } else {
            // Hiển thị dữ liệu
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                if (bestSellerProducts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Sách bán chạy",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 12.dp),
                        )
                        // Truyền dữ liệu thật đã được lọc
                        BestSellerCard(
                            bestSellerBooks = bestSellerProducts,
                            onBookClick = onBookClick
                        )
                    }
                }

                if (recommendedProducts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Đề xuất cho bạn",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 12.dp),
                        )
                        // Truyền dữ liệu thật đã được lọc
                        RecommendedList(
                            recommendedBooks = recommendedProducts,
                            onBookClick = onBookClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeHeader(
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(vertical = 8.dp)
            .height(60.dp)
            .background(Color(0xFFA7AAE1)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onProfileClick) {
            Image(
                painter = painterResource(R.drawable.profile_holder),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }
        IconButton(onClick = onSearchClick) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun BestSellerCard(
    bestSellerBooks: List<HomeUIProduct>,
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier) {
    Card(modifier = modifier
        .fillMaxWidth()
        .height(180.dp)
        .border(
            width = 4.dp,
            shape = RoundedCornerShape(12.dp),
            color = Color(color = 0xFFF2AEBB)
        ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5D3C4))
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = modifier
                .padding(top = 20.dp)
        ) {
            items(bestSellerBooks) { book -> //as best seller
                AsyncImage(
                    model = book.imageUrl, // URL ảnh từ API
                    contentDescription = book.name,
                    modifier = modifier
                        .width(100.dp)
                        .height(140.dp)
                        .clickable { onBookClick(book.id) },
                    contentScale = ContentScale.Crop,
                    // Có thể thêm Placeholder và Error Image ở đây nếu cần
                )
            }
        }
    }
}

@Composable
fun RecommendedCard(
    recommend: HomeUIProduct,
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .height(180.dp)
        .border(
            width = 4.dp,
            shape = RoundedCornerShape(12.dp),
            color = Color(color = 0xFFF2AEBB)
        )
        .clickable { onBookClick(recommend.id) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5D3C4))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = recommend.imageUrl, // URL ảnh
                contentDescription = recommend.name,
                modifier = Modifier.weight(1f)
                    .width(100.dp)
                    .height(140.dp)
                    .padding(start = 8.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier
                .weight(2f)
                .height(180.dp)
                .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = recommend.name,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 1
                )
                Text(
                    text = recommend.authorName,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp
                )
                Text(
                    text = recommend.price.toCurrencyString(),
                    modifier = Modifier.padding(8.dp),
                    color = Color.Red,
                    fontSize = 16.sp
                )
                Row (horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(8.dp)) {
                    RatingBar(rating = recommend.ratingAvg.toFloat())
                }
            }
        }
    }
}

@Composable
fun RatingBar(rating: Float, maxStars: Int = 5) {
    Row {
        for (i in 1..maxStars) {
            val starValue = rating - (i - 1)
            DisplayStar(starValue)
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
fun RecommendedList(
    recommendedBooks: List<HomeUIProduct>,
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        recommendedBooks.forEach { book ->
            RecommendedCard(
                recommend = book,
                onBookClick = onBookClick
                )
        }
    }
}

//@Preview
//@Composable
//fun HomeFragmentPreview(){
//    val sampleList = listOf(
//        UI_HomeBestSeller(imageID = R.drawable.book1),
//        UI_HomeBestSeller(imageID = R.drawable.book2)
//    )
//    BestSellerCard(sampleList,
//        onBookClick = {})
//}
//@Preview
//@Composable
//fun HomeFragmentPreview2(){
//    val sampleList = listOf(
//        UI_HomeRecommend(
//            author = R.string.book1_author,
//            price = R.string.book1_price,
//            name = R.string.book1_name,
//            imageID = R.drawable.book1,
//            star = 5f
//        ),
//        UI_HomeRecommend(
//            author = R.string.book2_author,
//            price = R.string.book2_price,
//            name = R.string.book2_name,
//            imageID = R.drawable.book2,
//            star = 3.5f
//        )
//    )
//    RecommendedList(sampleList,
//        onBookClick = {})
//}
//
//@Preview (
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun HomeFragmentPreview3(){
//    BookstoreTheme {
//        HomeScreen(onBookClick = {})
//    }
//}