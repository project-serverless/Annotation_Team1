package com.chattymin.threedays.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.chattymin.threedays.Frame.BoxFrame
import com.chattymin.threedays.Frame.DayBoolean
import com.chattymin.threedays.Frame.SmallBoxFrame
import com.chattymin.threedays.R
import com.chattymin.threedays.navigation.BottomScreen
import com.chattymin.threedays.navigation.FriendNavigationScreens
import com.chattymin.threedays.navigation.Screen
import com.chattymin.threedays.ui.theme.LightGreen
import com.chattymin.threedays.ui.theme.LightYellow

@Composable
fun FriendListScreen(navController: NavController) {
    var friendsList by rememberSaveable {
        mutableStateOf(3)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .background(color = LightGreen),
        //verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            IconTopView(isSetting = false, padding = 0)
        }
        items(friendsList) {
            Spacer(modifier = Modifier.height(6.dp))
            FriendItem(navController = navController)
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun FriendItem(navController: NavController) {
    BoxFrame(modifier = Modifier.clickable{
        navController.navigate(FriendNavigationScreens.FriendDetail.route)
    }) {
        Column(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                //horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = com.chattymin.threedays.R.drawable.cat),
                    contentDescription = "profile pic",
                    Modifier
                        .padding(end = 12.dp)
                        .size(width = 50.dp, height = 60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Column() {
                    Text(
                        text = "김혜빈",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = LightGreen
                    )
                    Text(
                        text = "하나씩 하나씩 이뤄가는 재미",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BoxFrame(size = 0.8f, color = LightYellow) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                            text = "1일 1커밋",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            DayBoolean(
                                modifier = Modifier.padding(12.dp),
                                text = "Day 1",
                                id = R.drawable.checkcircle_solid
                            )
                            DayBoolean(
                                modifier = Modifier.padding(12.dp),
                                text = "Day 2",
                                id = R.drawable.checkcircle_outline
                            )
                            DayBoolean(
                                modifier = Modifier.padding(12.dp),
                                text = "Day 3",
                                id = R.drawable.checkcircle_outline
                            )
                        }
                    }

                }
                Icon(
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            // 목표 달성 api 호출
                        },
                    painter = painterResource(id = com.chattymin.threedays.R.drawable.mission_before),
                    contentDescription = "send",
                )
            }
        }
    }
}

@Preview
@Composable
fun FriendListScreenPreview() {
    //FriendListScreen()
}