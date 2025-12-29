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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import com.example.bookstore.model.products.HomeUIProduct
import coil.compose.AsyncImage
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

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

    var searchOpen by remember { mutableStateOf(false) }
    val searchResults = viewModel.searchResults
    val searchQuery = viewModel.searchQuery

    // Phân loại sản phẩm (Ví dụ: Best Seller là những sản phẩm có tag "best_seller")
    val bestSellerProducts = products.filter { it.isBestSeller }
    val recommendedProducts = products.filter { !it.isBestSeller }

    Column(modifier = Modifier
        //.padding(16.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        HomeHeader(
            searchOpen = searchOpen,
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.onQueryChange(it) },
            onProfileClick = { /* Điều hướng profile */ },
            onSearchClick = { searchOpen = true },
            onCloseSearch = {
                searchOpen = false
                viewModel.onQueryChange("") // Reset khi đóng
            }
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
            if (searchOpen) {
                // Giao diện hiển thị kết quả tìm kiếm
                if (searchResults.isEmpty() && searchQuery.isNotEmpty()) {
                    Text("Không tìm thấy kết quả", modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
                        items(searchResults) { book ->
                            BookSearchItem(book = book, onClick = { onBookClick(book.id) })
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .padding(bottom = 80.dp)
                        .navigationBarsPadding()
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
}

@Composable
fun HomeHeader(
    searchOpen: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCloseSearch: () -> Unit
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
        if (!searchOpen) {
            IconButton(onClick = onProfileClick) {
                Image(
                    painter = painterResource(R.drawable.profile_holder),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                )
            }
            Text("Bookstore", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, null, tint = Color.DarkGray, modifier = Modifier.size(32.dp))
            }
        } else {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Tìm kiếm sách...", fontSize = 14.sp) },
                singleLine = true,
                modifier = Modifier.weight(1f).padding(start = 12.dp).height(50.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp)
            )
            IconButton(onClick = onCloseSearch) {
                Icon(Icons.Default.Close, null, tint = Color.White)
            }
        }
    }
}

@Composable
fun BookSearchItem(
    book: HomeUIProduct, // Sử dụng Model của bạn để đồng bộ
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = book.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, maxLines = 1)
                Text(text = book.authorName, fontSize = 13.sp, color = Color.Gray)
                Text(
                    text = book.price.toCurrencyString(),
                    fontSize = 14.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }
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
