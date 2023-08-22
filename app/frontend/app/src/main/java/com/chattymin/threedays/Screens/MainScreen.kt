package com.chattymin.threedays.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.chattymin.threedays.App
import com.chattymin.threedays.Frame.BoxFrame
import com.chattymin.threedays.Frame.DayBoolean
import com.chattymin.threedays.Frame.SmallBoxFrame
import com.chattymin.threedays.R
import com.chattymin.threedays.Retrofit.RetrofitManager
import com.chattymin.threedays.Utils.BackOnPressed
import com.chattymin.threedays.Utils.MESSAGE
import com.chattymin.threedays.Utils.RESPONSE_STATE
import com.chattymin.threedays.navigation.Screen
import com.chattymin.threedays.ui.theme.*

@Composable
fun MainScreen(navController: NavController) {
    BackOnPressed()

    RetrofitManager.instance.mainpage(
        completion = { responseState ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    //navController.navigate(Screen.Once.route)
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(App.instance, MESSAGE.ERROR, Toast.LENGTH_SHORT).show()
                }
            }
        })
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
            FriendTodayGoal()
        }
    }
}

@Composable
fun UserInfo() {
    BoxFrame {
        Column(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween//SpaceAround
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.cat),
                        contentDescription = "profile pic",
                        Modifier
                            .padding(end = 12.dp)
                            .size(width = 50.dp, height = 60.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "박동민님\n반갑습니다:)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = LightGreen
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            // 드롭다운 메뉴
                        },
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "send",
                    tint = LightGreen
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Goal(text = "🏆이룬 목표", count = "24")
                Goal(text = "\uD83C\uDFAF연속 달성", count = "4")
                Goal(text = "🔥목표 달성률", count = "75%")
                Goal(text = "\uD83D\uDC65주변 친구", count = "24")
            }
        }
    }
}

@Composable
fun Goal(text: String, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(text = count, fontSize = 40.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TodayGoal() {
    BoxFrame {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text(
                        text = "하루에 물 3잔 마시기",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "목표를 응원해요:)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = LightGreen
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            // 목표 달성 api 호출
                        },
                    painter = painterResource(id = R.drawable.mission_before),
                    contentDescription = "send",
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SmallBoxFrame(size = 100) {
                    DayBoolean(text = "Day 1", id = R.drawable.checkcircle_solid)
                }
                SmallBoxFrame(size = 100) {
                    DayBoolean(text = "Day 2", id = R.drawable.checkcircle_solid)
                }
                SmallBoxFrame(size = 100) {
                    DayBoolean(text = "Day 3", id = R.drawable.checkcircle_outline)
                }
            }
        }
    }
}

@Composable
fun FriendTodayGoal() {
    BoxFrame() {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "친구들의 목표들👀",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LightGreen
                )
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            // 드롭다운 메뉴
                        },
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "send",
                    tint = LightGreen
                )
            }
            BoxFrame(modifier = Modifier.padding(4.dp), color = LightYellow) {
                Column(modifier = Modifier.padding(4.dp)) {
                    Column(modifier = Modifier.padding(4.dp)) {
                        Text(modifier = Modifier, text = "김희연", color = Green, fontSize = 16.sp)
                        Text(modifier = Modifier, text = "하루 책 1권 읽기", color = Color.Black, fontSize = 12.sp)
                    }

                    Row() {
                        SmallBoxFrame(size = 100) {
                            DayBoolean(text = "Day 1", id = R.drawable.checkcircle_solid)
                        }
                        SmallBoxFrame(size = 100) {
                            DayBoolean(text = "Day 2", id = R.drawable.checkcircle_solid)
                        }
                        SmallBoxFrame(size = 100) {
                            DayBoolean(text = "Day 3", id = R.drawable.checkcircle_outline)
                        }
                    }
                }
            }
        }
    }
}
