package com.chattymin.threedays.Model

data class Friend(
    val Name: String,
    val Comment: String,
    val SuccessGoal: Int,
    val ContinueGoal: Int,
    val SuccessPercent: Int,
    val FriendCnt: Int,
    val Goal: String,
    val GoalArr: MutableList<Boolean>,
    val TodaySuccess: Boolean
)