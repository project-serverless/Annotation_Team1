package com.chattymin.threedays.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chattymin.threedays.App
import com.chattymin.threedays.Frame.*
import com.chattymin.threedays.R
import com.chattymin.threedays.Retrofit.RetrofitManager
import com.chattymin.threedays.Utils.*
import com.chattymin.threedays.navigation.Screen
import com.chattymin.threedays.ui.theme.*

@Composable
fun MainScreen(navController: NavController) {
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var isLogout by rememberSaveable {
        mutableStateOf(false)
    }

    // MainPageInfo 가져오기
    var Name by rememberSaveable {
        mutableStateOf("")
    }
    var SuccessGoal by rememberSaveable {
        mutableStateOf(0)
    }
    var ContinueGoal by rememberSaveable {
        mutableStateOf(0)
    }
    var SuccessPercent by rememberSaveable {
        mutableStateOf(0)
    }
    var FriendCnt by rememberSaveable {
        mutableStateOf(0)
    }
    var Goal by rememberSaveable {
        mutableStateOf("")
    }
    var TodaySuccess by rememberSaveable {
        mutableStateOf(false)
    }
    var FriendName by rememberSaveable {
        mutableStateOf("")
    }
    var FriendGoal by rememberSaveable {
        mutableStateOf("")
    }
    var userID by rememberSaveable {
        mutableStateOf("")
    }
    var friendId by rememberSaveable {
        mutableStateOf("")
    }
    var GoalArr by rememberSaveable {
        mutableStateOf(mutableListOf<Boolean>(false, false, false))
    }
    var FriendGoalArr by rememberSaveable {
        mutableStateOf(mutableListOf<Boolean>(false, false, false))
    }



    BackOnPressed()

    if (isLogout){
        AlertDialog(
            onDismissRequest = {
                isLogout = false
            },
            confirmButton = {
                Column() {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.Start) {
                        Text("정말로 로그아웃 하시겠습니까?")
                        Text("조금 더 목표를 이루어보아요:)")
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        RoundCornerFrame(
                            modifier = Modifier
                                .clickable {
                                    App.token_prefs.accessToken = ""
                                    RestartApp()
                                },
                            maxWidth = 0.4f,
                            borderColor = Green,
                            arrangement = Arrangement.Center
                        ){
                            Text(text = "로그아웃", color = Color.Red)
                        }
                        RoundCornerFrame(
                            modifier = Modifier
                                .clickable {
                                    isLogout = false
                                },
                            maxWidth = 0.7f,
                            borderColor = Green,
                            arrangement = Arrangement.Center
                        ){
                            Text(text = "취소", color = Color.Black)
                        }
                    }
                }

            },
            shape = RoundedCornerShape(12.dp),
            backgroundColor = LightGreen
        )
    }

    if (isLoading){
        LoadingCircle()
        RetrofitManager.instance.mainpage(
            completion = { responseState, info ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Name = info!!.Name
                        SuccessGoal = info.SuccessGoal
                        ContinueGoal = info.ContinueGoal
                        SuccessPercent = info.SuccessPercent
                        FriendCnt = info.FriendCnt
                        Goal = info.Goal
                        TodaySuccess = info.TodaySuccess
                        FriendName = info.FriendName
                        FriendGoal = info.FriendGoal
                        userID = info.userId
                        friendId = info.friendId
                        GoalArr = info.GoalArr
                        FriendGoalArr = info.FriendGoalArr
                        isLoading = false
                    }
                    RESPONSE_STATE.FAIL -> {
                        Toast.makeText(App.instance, MESSAGE.ERROR, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = LightGreen),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            IconTopView(true){isLogout = true}
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                UserInfo(Name = Name, SuccessGoal = SuccessGoal, ContinueGoal = ContinueGoal, SuccessPercent = SuccessPercent, FriendCnt = FriendCnt)
                TodayGoal(Goal = Goal, TodaySuccess = TodaySuccess, GoalArr = GoalArr)
                FriendTodayGoal(FriendName = FriendName, FriendGoal = FriendGoal, FriendGoalArr = FriendGoalArr)
            }
        }
    }
}

@Composable
fun UserInfo(Name: String, SuccessGoal: Int, ContinueGoal: Int, SuccessPercent: Int, FriendCnt: Int) {
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
                        text = "${Name}님\n반갑습니다:)",
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
                Goal(text = "🏆이룬 목표", count = SuccessGoal.toString())
                Goal(text = "\uD83C\uDFAF연속 달성", count = ContinueGoal.toString())
                Goal(text = "🔥목표 달성률", count = "${SuccessPercent}%")
                Goal(text = "\uD83D\uDC65주변 친구", count = FriendCnt.toString())
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
fun TodayGoal(Goal: String, TodaySuccess: Boolean, GoalArr: MutableList<Boolean>) {
    if (Goal == "NONE") {
        var Goal = remember { mutableStateOf("") }
        BoxFrame {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "지금바로 목표를 정하고 3일동안 지켜봐요!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = LightGreen
                )
                SearchBar2(searchText = Goal){
                    Goal.value = it
                }
                RoundCornerFrame(
                    modifier = Modifier.clickable {
                        /*
                        RetrofitManager.instance.login(
                            ID = ID.value,
                            PW = PW.value,
                            completion = { responseState ->
                                when (responseState) {
                                    RESPONSE_STATE.OKAY -> {
                                        navController.navigate(Screen.Once.route)
                                    }
                                    RESPONSE_STATE.FAIL -> {
                                        Toast.makeText(App.instance, MESSAGE.ERROR, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            })

                         */
                    },
                    borderColor = Green,
                    arrangement = Arrangement.Center
                ) {
                    Text(text = "목표 정하러 가기", color = Green)
                }
            }
        }
    }
    else{
        BoxFrame {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(
                            text = Goal,
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
                    if (TodaySuccess) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp),
                            painter = painterResource(id = R.drawable.mission_after),
                            contentDescription = "send",
                            tint = Color.Unspecified
                        )
                    }else{
                        Icon(
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    // api 호출
                                },
                            painter = painterResource(id = R.drawable.mission_before),
                            contentDescription = "send",
                            tint = Color.Unspecified
                        )
                    }


                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SmallBoxFrame(size = 100) {
                        DayBoolean(text = "Day 1", id = if (GoalArr[0]) R.drawable.checkcircle_solid else R.drawable.checkcircle_outline)
                    }
                    SmallBoxFrame(size = 100) {
                        DayBoolean(text = "Day 2", id = if (GoalArr[1]) R.drawable.checkcircle_solid else R.drawable.checkcircle_outline)
                    }
                    SmallBoxFrame(size = 100) {
                        DayBoolean(text = "Day 3", id = if (GoalArr[2]) R.drawable.checkcircle_solid else R.drawable.checkcircle_outline)
                    }
                }
            }
        }
    }
}

@Composable
fun FriendTodayGoal(FriendName: String, FriendGoal: String, FriendGoalArr: MutableList<Boolean>) {
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
                    Column(modifier = Modifier.padding(top = 4.dp, start = 8.dp, bottom = 4.dp)) {
                        Text(modifier = Modifier, text = FriendName, color = Green, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(modifier = Modifier, text = FriendGoal, color = Color.Black, fontSize = 12.sp)
                    }

                    Row() {
                        SmallBoxFrame(size = 100) {
                            DayBoolean(text = "Day 1", id = if (FriendGoalArr[0]) R.drawable.checkcircle_solid else R.drawable.checkcircle_outline)
                        }
                        SmallBoxFrame(size = 100) {
                            DayBoolean(text = "Day 2", id = if (FriendGoalArr[1]) R.drawable.checkcircle_solid else R.drawable.checkcircle_outline)
                        }
                        SmallBoxFrame(size = 100) {
                            DayBoolean(text = "Day 3", id = if (FriendGoalArr[2]) R.drawable.checkcircle_solid else R.drawable.checkcircle_outline)
                        }
                    }
                }
            }
        }
    }
}
