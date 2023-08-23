package com.chattymin.threedays.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chattymin.threedays.App
import com.chattymin.threedays.Frame.*
import com.chattymin.threedays.R
import com.chattymin.threedays.Retrofit.RetrofitManager
import com.chattymin.threedays.Utils.*
import com.chattymin.threedays.navigation.BottomScreen
import com.chattymin.threedays.navigation.Screen
import com.chattymin.threedays.ui.theme.*
import kotlin.math.exp

@Composable
fun MainScreen(navController: NavController) {
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var isLogout by rememberSaveable {
        mutableStateOf(false)
    }

    // MainPageInfo 가져오기
    var Name = rememberSaveable {
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
    var FriendId by rememberSaveable {
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
                        Name.value = info!!.Name
                        SuccessGoal = info.SuccessGoal
                        ContinueGoal = info.ContinueGoal
                        SuccessPercent = info.SuccessPercent
                        FriendCnt = info.FriendCnt
                        Goal = info.Goal
                        TodaySuccess = info.TodaySuccess
                        FriendName = info.FriendName
                        FriendGoal = info.FriendGoal
                        userID = info.userId
                        FriendId = info.FriendId
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
                UserInfo(Name = Name, SuccessGoal = SuccessGoal, ContinueGoal = ContinueGoal, SuccessPercent = SuccessPercent, FriendCnt = FriendCnt, userId = userID)
                TodayGoal(navController = navController, Goal = Goal, TodaySuccess = TodaySuccess, GoalArr = GoalArr)
                FriendTodayGoal(FriendName = FriendName, FriendGoal = FriendGoal, FriendGoalArr = FriendGoalArr)
            }
        }
    }
}

@Composable
fun UserInfo(Name: MutableState<String>, SuccessGoal: Int, ContinueGoal: Int, SuccessPercent: Int, FriendCnt: Int, userId: String, Comment: MutableState<String> = mutableStateOf(""), isMine: Boolean = true) {
    var expanded by remember { mutableStateOf(false) }
    var changeInfo by remember { mutableStateOf(false) }
    var changeInfo2 by remember { mutableStateOf(false) }

    var NameTemp = remember { mutableStateOf(Name.value) }
    var CommentTemp = remember { mutableStateOf(Comment.value) }

    if (changeInfo2){
        AlertDialog(
            onDismissRequest = {
                changeInfo2 = false
            },
            confirmButton = {
                Column() {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.Start) {
                        Text("정보 수정 완료!")
                        Text("작심3일을 마음껏 즐겨주세요 :)")
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        RoundCornerFrame(
                            modifier = Modifier
                                .clickable {
                                    changeInfo2 = false
                                },
                            maxWidth = 0.5f,
                            borderColor = Green,
                            arrangement = Arrangement.Center
                        ){
                            Text(text = "확인", color = Green)
                        }
                    }
                }

            },
            shape = RoundedCornerShape(12.dp),
            backgroundColor = LightGreen
        )
    }

    if (changeInfo){
        AlertDialog(
            onDismissRequest = {
                changeInfo = false
            },
            confirmButton = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    changeInfo = false
                                },
                            painter = painterResource(id = R.drawable.x),
                            contentDescription = "popDown",
                            tint = Color.Black
                        )
                    }
                    Text(modifier = Modifier.padding(24.dp), text = "내 정보 수정", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    SearchBar(text = "이름", searchText = NameTemp, imeAction = ImeAction.Next){
                        NameTemp.value = it
                    }
                    SearchBar(text = "소개글",searchText = Comment, imeAction = ImeAction.Done){
                        CommentTemp.value = it
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        RoundCornerFrame(
                            modifier = Modifier
                                .clickable {
                                    changeInfo2 = true
                                    RetrofitManager.instance.updateUserInfo(
                                        NameTemp.value,
                                        CommentTemp.value,
                                        completion = { responseState ->
                                            when (responseState) {
                                                RESPONSE_STATE.OKAY -> {
                                                    Name.value = NameTemp.value
                                                    changeInfo = false
                                                }
                                                RESPONSE_STATE.FAIL -> {
                                                    Toast.makeText(App.instance, MESSAGE.ERROR, Toast.LENGTH_SHORT)
                                                        .show()
                                                }
                                            }
                                        })
                                },
                            maxWidth = 0.6f,
                            borderColor = Green,
                            arrangement = Arrangement.Center
                        ){
                            Text(text = "수정하기", color = Green)
                        }
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            backgroundColor = LightGreen
        )
    }

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
                        text = "${Name.value}님\n" + if (Comment.value.isEmpty()) "반갑습니다:)" else Comment.value,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = LightGreen
                    )
                }
                if (isMine) {
                    Column() {
                        Icon(
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    // 드롭다운 메뉴
                                    expanded = !expanded
                                },
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = "send",
                            tint = LightGreen
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    changeInfo = true
                                    expanded = false
                                }) {
                                Text(text = "내 정보 수정")
                            }
                            DropdownMenuItem(
                                onClick = {
                                    CopyToClipboard(App.instance, userId)
                                    expanded = false
                                }) {
                                Text(text = "내 ID 공유하기")
                            }
                        }
                    }
                }
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
fun TodayGoal(navController: NavController, Goal: String, TodaySuccess: Boolean, GoalArr: MutableList<Boolean>, isMine: Boolean = true) {
    var isUpdate by rememberSaveable {
        mutableStateOf(false)
    }

    if (isUpdate){
        AlertDialog(
            onDismissRequest = {
                isUpdate = false
            },
            confirmButton = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(modifier = Modifier.padding(24.dp), text = "오늘 미션 완료!!", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        RoundCornerFrame(
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(BottomScreen.Main.screenRoute)
                                },
                            maxWidth = 0.6f,
                            borderColor = Green,
                            arrangement = Arrangement.Center
                        ){
                            Text(text = "확인", color = Green)
                        }
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            backgroundColor = LightGreen
        )
    }

    if (Goal == "NONE" && isMine){
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
                                    if (isMine) {
                                        isUpdate = true
                                        RetrofitManager.instance.successTodayGoal(
                                            completion = { responseState ->
                                                when (responseState) {
                                                    RESPONSE_STATE.OKAY -> {}
                                                    RESPONSE_STATE.FAIL -> {
                                                        Toast
                                                            .makeText(
                                                                App.instance,
                                                                MESSAGE.ERROR,
                                                                Toast.LENGTH_SHORT
                                                            )
                                                            .show()
                                                    }
                                                }
                                            })
                                    }
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
    var expanded by remember { mutableStateOf(false) }

    var addFriend by remember { mutableStateOf(false) }

    if (addFriend){
        var friendId = remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = {
                addFriend = false
            },
            text = {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Column(modifier = Modifier
                            .padding(start = 12.dp, bottom = 12.dp, end = 12.dp), horizontalAlignment = Alignment.Start) {
                            Text("친구의 ID를 입력해주세요 :)", fontWeight = FontWeight.Bold)
                        }
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    addFriend = false
                                },
                            painter = painterResource(id = R.drawable.x),
                            contentDescription = "popDown",
                            tint = Color.Black
                        )
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                        SearchBar(text = "ID", searchText = friendId, maxWidth = 0.9f, imeAction = ImeAction.Done,onSearch = {})
                        Icon(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                },
                            painter = painterResource(id = R.drawable.send_fill),
                            contentDescription = "send",
                            tint = Green
                        )
                    }
                }

            },
            confirmButton = {},
            shape = RoundedCornerShape(12.dp),
            backgroundColor = LightGreen
        )
    }


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
                Column() {
                    Icon(
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                expanded = !expanded
                            },
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "send",
                        tint = LightGreen
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                addFriend = true
                                expanded = false
                            }) {
                            Text(text = "친구 추가하기")
                        }
                    }
                }
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
