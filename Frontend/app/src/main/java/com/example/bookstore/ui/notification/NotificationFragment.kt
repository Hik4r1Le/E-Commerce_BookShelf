package com.example.bookstore.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.fragment.app.commit
import com.example.bookstore.R
import com.example.bookstore.model.NotificationItem

class NotificationFragment : Fragment() {

    companion object {
        fun open(from: Fragment) {
            from.parentFragmentManager.commit {
                replace(android.R.id.content, NotificationFragment())
                addToBackStack(null)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    NotificationScreen(
                        notifications = sampleNotifications(),
                        onBack = {
                            requireActivity()
                                .onBackPressedDispatcher
                                .onBackPressed()
                        }
                    )
                }
            }
        }
    }
}

// HEADER
@Composable
fun NotificationHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB6B8E6))
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(22.dp)
                .clickable { onBack() }
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "THÔNG BÁO",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

// CARD
@Composable
fun NotificationCard(item: NotificationItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1E8)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = item.title,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(item.message, fontSize = 13.sp)

                Spacer(modifier = Modifier.height(6.dp))

                Text(item.time, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

// SCREEN
@Composable
fun NotificationScreen(
    notifications: List<NotificationItem>,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        NotificationHeader(onBack)
        LazyColumn {
            items(notifications) {
                NotificationCard(it)
            }
        }
    }
}

// DATA
fun sampleNotifications() = listOf(
    NotificationItem(
        id = 1,
        title = "Giao hàng thành công",
        message = "Đơn hàng SPXVN0123 đã giao thành công đến bạn vào ngày 20-12-2025.",
        time = "11:30 20-12-2025",
        imageRes = R.drawable.book6
    ),
    NotificationItem(
        id = 2,
        title = "Đã gửi cho đơn vị vận chuyển",
        message = "Đơn hàng SPXVN0123 đã được gửi cho đơn vị vận chuyển. Ngày giao hàng dự kiến là 20-12-2025.",
        time = "11:00 19-12-2025",
        imageRes = R.drawable.book6
    ),
    NotificationItem(
        id = 3,
        title = "Đặt hàng thành công",
        message = "Đơn hàng SPXVN0123 đã được đặt thành công. Ngày giao hàng dự kiến là 20-12-2025.",
        time = "10:00 18-12-2025",
        imageRes = R.drawable.book6
    )
)

// PREVIEW
@Preview(showBackground = true)
@Composable fun PreviewHeader() { NotificationHeader {} }

@Preview(showBackground = true)
@Composable fun PreviewDelivered() { NotificationCard(sampleNotifications()[0]) }

@Preview(showBackground = true)
@Composable fun PreviewShipping() { NotificationCard(sampleNotifications()[1]) }

@Preview(showBackground = true)
@Composable fun PreviewOrderSuccess() { NotificationCard(sampleNotifications()[2]) }

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PreviewFullScreen() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            NotificationScreen(sampleNotifications()) {}
        }
    }
}