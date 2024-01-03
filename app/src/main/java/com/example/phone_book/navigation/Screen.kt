package com.example.phone_book.navigation

sealed class Screen(val route: String) {
    data object ContactList: Screen(route = "contact-list_screen")
}