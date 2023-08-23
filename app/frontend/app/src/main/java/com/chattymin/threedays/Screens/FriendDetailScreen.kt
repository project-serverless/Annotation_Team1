package com.chattymin.threedays.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chattymin.threedays.App
import com.chattymin.threedays.Model.FriendInfo
import com.chattymin.threedays.Retrofit.RetrofitManager
import com.chattymin.threedays.Utils.LoadingCircle
import com.chattymin.threedays.Utils.MESSAGE
import com.chattymin.threedays.Utils.RESPONSE_STATE
import com.chattymin.threedays.ui.theme.LightGreen

@Composable
fun FriendDetailScreen(navController: NavController, userId: String) {
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var Name = rememberSaveable {
        mutableStateOf("")
    }
    var Comment = rememberSaveable {
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
    var GoalArr by rememberSaveable {
        mutableStateOf(mutableListOf<Boolean>(false, false, false))
    }

    if (isLoading){
        LoadingCircle()

        RetrofitManager.instance.friendDetails(
            userId,
            completion = { responseState, info->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {

                        Name.value = info!!.Name
                        Comment.value = info.Comment
                        SuccessGoal = info.SuccessGoal
                        ContinueGoal = info.ContinueGoal
                        SuccessPercent = info.SuccessPercent
                        FriendCnt = info.FriendCnt
                        Goal = info.Goal
                        TodaySuccess = info.TodaySuccess
                        GoalArr = info.GoalArr

                        isLoading = false
                    }
                    RESPONSE_STATE.FAIL -> {
                        Toast.makeText(App.instance, MESSAGE.ERROR, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = LightGreen),
        ) {
            IconTopView(false) {}
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                //verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                UserInfo(Name = Name, SuccessGoal = SuccessGoal, ContinueGoal, SuccessPercent = SuccessPercent, FriendCnt = FriendCnt, userId, Comment = Comment, isMine = false)
                Spacer(modifier = Modifier.height(12.dp))
                TodayGoal(navController, Goal = Goal, TodaySuccess = TodaySuccess, GoalArr = GoalArr, false)
            }
        }
    }
}