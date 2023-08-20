package com.chattymin.threedays.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Once : Screen("once_screen")
}