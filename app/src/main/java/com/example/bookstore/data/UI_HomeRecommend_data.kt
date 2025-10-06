package com.example.bookstore.data

import com.example.bookstore.R
import com.example.bookstore.model.UI_HomeRecommend

class UI_HomeRecommend_data() {
    fun loadData(): List<UI_HomeRecommend>{
        return listOf<UI_HomeRecommend>(
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
    }
}