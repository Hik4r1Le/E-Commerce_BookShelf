package com.example.bookstore.ui.book_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookDetailViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is book detail Fragment"
    }
    val text: LiveData<String> = _text
}