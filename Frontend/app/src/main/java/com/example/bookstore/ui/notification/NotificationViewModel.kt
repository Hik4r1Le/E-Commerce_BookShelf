package com.example.bookstore.ui.notification

import androidx.lifecycle.ViewModel
import com.example.bookstore.model.NotificationDto
import com.example.bookstore.model.NotificationItem
import com.example.bookstore.model.toUi
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationViewModel : ViewModel() {

    private val database =
        FirebaseDatabase.getInstance().reference.child("notifications")

    private val _notifications =
        MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> =
        _notifications

    init {
        listenNotifications()
    }

    private fun listenNotifications() {
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull {
                    it.getValue(NotificationDto::class.java)
                }.map {
                    it.toUi()
                }
                _notifications.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}