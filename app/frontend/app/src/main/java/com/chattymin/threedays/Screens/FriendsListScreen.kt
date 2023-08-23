package com.chattymin.threedays.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.chattymin.threedays.App
import com.chattymin.threedays.Frame.BoxFrame
import com.chattymin.threedays.Frame.DayBoolean
import com.chattymin.threedays.Frame.SmallBoxFrame
import com.chattymin.threedays.Model.FriendInfo
import com.chattymin.threedays.R
import com.chattymin.threedays.Retrofit.RetrofitManager
import com.chattymin.threedays.Utils.LoadingCircle
import com.chattymin.threedays.Utils.MESSAGE
import com.chattymin.threedays.Utils.RESPONSE_STATE
import com.chattymin.threedays.navigation.BottomScreen
import com.chattymin.threedays.navigation.FriendNavigationScreens
import com.chattymin.threedays.navigation.Screen
import com.chattymin.threedays.ui.theme.LightGreen
import com.chattymin.threedays.ui.theme.LightYellow

@Composable
fun FriendListScreen(navController: NavController) {
    var FriendInfos by rememberSaveable { mutableStateOf(MutableList(0) { FriendInfo() }) }

    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }

    if (isLoading){
        LoadingCircle()
        RetrofitManager.instance.friendList(
            completion = { responseState, friendsList ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        FriendInfos = friendsList!!
                        isLoading = false
                    }
                    RESPONSE_STATE.FAIL -> {
                        Toast.makeText(App.instance, MESSAGE.ERROR, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = LightGreen)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                IconTopView(isSetting = false, padding = 0) {}
            }
            items(FriendInfos) {
                Spacer(modifier = Modifier.height(6.dp))
                FriendItem(navController = navController, friendInfo = it)
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
fun FriendItem(navController: NavController, friendInfo: FriendInfo) {
    BoxFrame(modifier = Modifier.clickable{
        navController.navigate(FriendNavigationScreens.FriendDetail.route+"/${friendInfo.UserId}")
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
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cat),
                    contentDescription = "profile pic",
                    Modifier
                        .padding(end = 12.dp)
                        .size(width = 50.dp, height = 60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Column() {
                    Text(
                        text = friendInfo.Name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = LightGreen
                    )
                    Text(
                        text = friendInfo.Comment,
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
                            text = friendInfo.Goal,
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
                                id = if (friendInfo.GoalArr[0]) R.drawable.checkcircle_solid else R.drawable.checkcircle_outline
                            )
                            DayBoolean(
                                modifier = Modifier.padding(12.dp),
                                text = "Day 2",
                                id = if (friendInfo.GoalArr[1]) R.drawable.checkcircle_solid else R.drawable.checkcircle_outline
                            )
                            DayBoolean(
                                modifier = Modifier.padding(12.dp),
                                text = "Day 3",
                                id = if (friendInfo.GoalArr[2]) R.drawable.checkcircle_solid else R.drawable.checkcircle_outline
                            )
                        }
                    }

                }
                Icon(
                    modifier = Modifier
                        .size(60.dp),
                    painter = painterResource(id = if (friendInfo.TodaySuccess) R.drawable.mission_after else R.drawable.mission_before),
                    contentDescription = "send",
                    tint = Color.Unspecified
                )
            }
        }
    }
}
