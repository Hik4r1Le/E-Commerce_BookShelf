package com.example.bookstore.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class UI_HomeRecommend(
    @StringRes val author: Int,
    @StringRes val price: Int,
    @StringRes val name: Int,
    @DrawableRes val imageID: Int,
    val star: Float //rating or so
)