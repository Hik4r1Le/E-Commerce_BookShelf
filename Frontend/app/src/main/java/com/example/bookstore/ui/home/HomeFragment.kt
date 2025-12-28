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
import com.example.bookstore.model.UI_HomeBestSeller
import com.example.bookstore.model.UI_HomeRecommend
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.bookstore.data.UI_HomeBestSeller_data
import com.example.bookstore.data.UI_HomeRecommend_data
import com.example.bookstore.ui.theme.BookstoreTheme

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {
                HomeScreen(
                    onBookClick = {
                        findNavController().navigate(R.id.home_to_bookDetail)
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(onBookClick: () -> Unit,
               viewModel: HomeViewModel = viewModel()) {
    var searchOpen by remember { mutableStateOf(false) }
    val searchResults by viewModel.searchResults.collectAsState()
    Column(
        modifier = Modifier
            //.padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        HomeHeader(
            searchOpen = searchOpen,
            onProfileClick = {},
            onSearchClick = { searchOpen = true },
            onCloseSearch = {
                searchOpen = false
                viewModel.onQueryChange("") // reset search
            },
            viewModel = viewModel
            //modifier = Modifier.weight(1f)
        )
        if (searchOpen) {
            if (searchResults.isEmpty()) {
                Text(
                    text = "No results found",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn {
                    items(searchResults) { book ->
                        book.name?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                item {
                    Text(
                        text = "Sách bán chạy",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 12.dp),
                    )
                    BestSellerCard(
                        bestSellerBooks = UI_HomeBestSeller_data().loadData(),
                        onBookClick = onBookClick
                    )
                }
                item {
                    Text(
                        text = "Đề xuất cho bạn",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 12.dp),
                    )
                    RecommendedList(
                        recommendedBooks = UI_HomeRecommend_data().loadData(),
                        onBookClick = onBookClick
                    )
                }
            }
        }
    }
}

@Composable
fun HomeHeader(
    searchOpen: Boolean,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCloseSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val query by viewModel.query.collectAsState()
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
        } else {
            TextField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                placeholder = {
                    Text(
                        text = "Search books…",
                        fontSize = 12.sp
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 12.sp
                ),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            IconButton(onClick = onCloseSearch) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close search"
                )
            }
        }
    }
}

@Composable
fun BestSellerCard(
    bestSellerBooks: List<UI_HomeBestSeller>,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier) {
    Card(modifier = modifier
        .fillMaxWidth()
        .height(180.dp)
        .border(
            width = 4.dp,
            shape = RoundedCornerShape(12.dp),
            color = Color(color = 0xFFF2AEBB)
        )
        .clickable { onBookClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5D3C4))
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = modifier
                .padding(top = 20.dp)
        ) {
            items(bestSellerBooks) { book -> //as best seller
                Image(
                    painter = painterResource(book.imageID),
                    contentDescription = null,
                    modifier = modifier
                        .width(100.dp)
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun RecommendedCard(recommend: UI_HomeRecommend,
                    onBookClick: () -> Unit,
                    modifier: Modifier = Modifier) {
    Card(modifier = modifier
        .fillMaxWidth()
        .height(180.dp)
        .border(
            width = 4.dp,
            shape = RoundedCornerShape(12.dp),
            color = Color(color = 0xFFF2AEBB)
        )
        .clickable { onBookClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5D3C4))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(recommend.imageID),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .width(100.dp)
                    .height(140.dp)
                    .padding(start = 8.dp),

            )
            Column(modifier = Modifier
                .weight(2f)
                .height(180.dp)
                .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(recommend.name),
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 1
                )
                Text(
                    text = stringResource(recommend.author),
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp
                )
                Text(
                    text = stringResource(recommend.price),
                    modifier = Modifier.padding(8.dp),
                    color = Color.Red,
                    fontSize = 16.sp
                )
                Row (horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(8.dp)) {
                    for (i in 1..5) {
                        val starValue = recommend.star - (i - 1)
                        DisplayStar(starValue)
                    }
                }
            }
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
fun RecommendedList(recommendedBooks: List<UI_HomeRecommend>,
                    onBookClick: () -> Unit,
                    modifier: Modifier = Modifier) {
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

@Preview
@Composable
fun HomeFragmentPreview(){
    val sampleList = listOf(
        UI_HomeBestSeller(imageID = R.drawable.book1),
        UI_HomeBestSeller(imageID = R.drawable.book2)
    )
    BestSellerCard(sampleList,
        onBookClick = {})
}
@Preview
@Composable
fun HomeFragmentPreview2(){
    val sampleList = listOf(
        UI_HomeRecommend(
            author = R.string.book1_author,
            price = R.string.book1_price,
            name = R.string.book1_name,
            imageID = R.drawable.book1,
            star = 5f
        ),
        UI_HomeRecommend(
            author = R.string.book2_author,
            price = R.string.book2_price,
            name = R.string.book2_name,
            imageID = R.drawable.book2,
            star = 3.5f
        )
    )
    RecommendedList(sampleList,
        onBookClick = {})
}

@Preview (
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun HomeFragmentPreview3(){
    BookstoreTheme {
        HomeScreen(onBookClick = {})
    }
}