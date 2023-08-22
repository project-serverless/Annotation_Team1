package com.chattymin.threedays.Utils

import android.content.Intent
import com.chattymin.threedays.App

fun RestartApp() {
    App.token_prefs.accessToken = ""
    val context = App.instance
    val packageManager = context.packageManager
    val launchIntent = packageManager.getLaunchIntentForPackage(context.packageName)
    launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(launchIntent)
}