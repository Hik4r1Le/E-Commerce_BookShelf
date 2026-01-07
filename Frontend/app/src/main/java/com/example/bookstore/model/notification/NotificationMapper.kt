package com.example.bookstore.model.notification

import com.example.bookstore.R

fun NotificationDto.toUi(): NotificationItem {
    return NotificationItem(
        id = id,
        title = title,
        message = message,
        time = time,
        imageRes = R.drawable.book6
    )
}