package com.chattymin.threedays.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomScreen(
    val title: String,
    val iconOutline: ImageVector,
    val iconSolid: ImageVector,
    val screenRoute: String
) {
    object Main : BottomScreen("메인", Icons.Outlined.Home, Icons.Filled.Home, "main")

    object Friend :
        BottomScreen("친구", Icons.Outlined.Person, Icons.Filled.Person, "friendList")
}

sealed class MainNavigationScreens(val route: String) {
    object Main : MainNavigationScreens("main")
    // object MainReview : MainNavigationScreens("mainReview")
}

sealed class FriendNavigationScreens(val route: String) {
    object FriendList : FriendNavigationScreens("friendList")
}