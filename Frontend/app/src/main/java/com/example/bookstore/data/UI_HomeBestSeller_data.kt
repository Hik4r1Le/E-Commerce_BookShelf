package com.example.bookstore.data
import com.example.bookstore.R
import com.example.bookstore.model.UI_HomeBestSeller

class UI_HomeBestSeller_data() {
    fun loadData(): List<UI_HomeBestSeller>{
        return listOf<UI_HomeBestSeller>(
            UI_HomeBestSeller(imageID = R.drawable.book1),
            UI_HomeBestSeller(imageID = R.drawable.book2),
        )
    }
}
