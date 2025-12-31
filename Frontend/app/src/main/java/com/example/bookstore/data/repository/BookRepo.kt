package com.example.bookstore.data.repository

import com.example.bookstore.data.remote.BookApi
import com.example.bookstore.model.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookRepo(
    private val api: BookApi
) {

    fun searchBooksByName(query: String): Flow<List<SearchResult>> = flow {

        val response = api.searchBooks(
            filter = "name:$query"
        )

        val results = response.data.map { item ->
            val p = item.product
            SearchResult(
                id = p.id,
                name = p.name,
                author_name = p.author_name,
                price = p.price,
                image_url = p.image_url
            )
        }

        emit(results)
    }
}
