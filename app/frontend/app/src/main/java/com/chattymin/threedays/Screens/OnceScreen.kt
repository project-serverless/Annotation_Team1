package com.chattymin.threedays.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chattymin.threedays.Navigation.MainScreenView
import com.chattymin.threedays.navigation.BottomScreen
import com.chattymin.threedays.ui.theme.LightGreen

@Composable
fun OnceScreen(
    startDestination: String = BottomScreen.Main.screenRoute
) {
    Box(modifier = Modifier.fillMaxSize().background(LightGreen))
    MainScreenView(startDestination)
}