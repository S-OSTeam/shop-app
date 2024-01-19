package com.example.deamhome.presentation.main

import androidx.annotation.IdRes
import com.example.deamhome.R

sealed class ScreenType(val tag: String, val isChangeBottomTab: Boolean) {
    sealed class ChangeScreenType(tag: String) : ScreenType(tag, true) {
        data object Home : ChangeScreenType("HOME")
        data object MyPage : ChangeScreenType("MY_PAGE")
    }

    sealed class PopScreenType(tag: String) : ScreenType(tag, false) {
        data object Search : PopScreenType("SEARCH")
        data object Category : PopScreenType("CATEGORY")
        data object Cart : PopScreenType("CART")
    }

    companion object {
        fun of(@IdRes id: Int): ScreenType {
            return when (id) {
                R.id.menu_item_home -> ChangeScreenType.Home
                R.id.menu_item_my_page -> ChangeScreenType.MyPage
                R.id.menu_item_search -> PopScreenType.Search
                R.id.menu_item_category -> PopScreenType.Category
                R.id.menu_item_cart -> PopScreenType.Cart
                else -> ChangeScreenType.Home
            }
        }
    }
}
