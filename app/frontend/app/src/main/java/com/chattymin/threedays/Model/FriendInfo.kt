package com.chattymin.threedays.Model

data class FriendInfo(
    val Name: String = "",
    val UserId: String = "",
    val Comment: String = "",
    val TodaySuccess: Boolean = false,
    val GoalArr: MutableList<Boolean> = mutableListOf(),
    val Goal: String = ""
)