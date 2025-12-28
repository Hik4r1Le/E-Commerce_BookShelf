package com.example.bookstore.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.remote.ApiClient
import com.example.bookstore.data.repository.BookRepo
import com.example.bookstore.model.SearchResult
import kotlinx.coroutines.flow.*

class HomeViewModel : ViewModel() {

    private val repository = BookRepo(ApiClient.api)

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    fun onQueryChange(text: String) {
        _query.value = text
    }

    val searchResults: StateFlow<List<SearchResult>> =
        query
            .debounce(500)
            .filter { it.length >= 2 }
            .distinctUntilChanged()
            .flatMapLatest { q ->
                repository.searchBooksByName(q)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}
