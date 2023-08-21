package com.chattymin.threedays.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chattymin.threedays.ui.theme.LightGreen

@Composable
fun FriendDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightGreen),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        IconTopView(true)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UserInfo()
            TodayGoal()
            //FriendTodayGoal()
        }
    }
}