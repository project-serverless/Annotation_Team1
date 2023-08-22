package com.chattymin.threedays.Model

data class MainPageInfo(
    val Name: String,
    val SuccessGoal: Int,
    val ContinueGoal: Int,
    val SuccessPercent: Int,
    val FriendCnt: Int,
    val Goal: String,
    val TodaySuccess: Boolean,
    val GoalArr: MutableList<Boolean>,
    val FriendName: String,
    val FriendGoal: String,
    val FriendGoalArr: MutableList<Boolean>,
    val userId: String,
    val FriendId: String
)
